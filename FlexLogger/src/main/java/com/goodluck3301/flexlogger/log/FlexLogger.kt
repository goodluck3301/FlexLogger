package com.goodluck3301.flexlogger.log

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.StringReader
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource
import kotlin.system.exitProcess

/**
 * FlexLogger: A flexible, configurable logging utility for Android.
 * This is a singleton object that holds the configuration and handles logging operations.
 */
object FlexLogger {

    private const val JSON_INDENT = 2
    private var config = LogConfig()
    private val dateFormatter = SimpleDateFormat(config.timestampFormat, Locale.US)

    /**
     * Initializes and configures the logger using a Kotlin DSL.
     * This must be called once, typically in your Application's `onCreate` method.
     *
     * @param block A lambda with `LogConfig` as its receiver for configuration.
     */
    fun init(block: LogConfig.() -> Unit) {
        val newConfig = LogConfig().apply(block)
        this.config = newConfig
        // Update date formatter if a custom format was provided
        if (newConfig.timestampFormat != "yyyy-MM-dd HH:mm:ss.SSS") {
            dateFormatter.applyPattern(newConfig.timestampFormat)
        }

        if (newConfig.enableCrashLogging && newConfig.destinations.any { it is BaseFileDestination }) {
            registerCrashLogger(newConfig.crashLogSize)
        }
    }

    /**
     * Registers a custom crash logger to capture uncaught exceptions and log them
     * with a configurable level of detail.
     *
     * @param logSize The verbosity level for crash logs:
     * - [CrashLogSize.SMALL]   : Only the crash message.
     * - [CrashLogSize.MEDIUM]  : Crash message, thread name, and top stack trace element.
     * - [CrashLogSize.LARGE]   : Full stack trace.
     *
     * The method replaces the default [Thread.UncaughtExceptionHandler] with a custom one.
     * It delegates to the previously registered handler after logging, or terminates the process
     * if no previous handler exists.
     *
     * This should be called early in the application lifecycle (e.g., in `Application.onCreate()`).
     */
    private fun registerCrashLogger(logSize: CrashLogSize) {
        val previousHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            val crashMessage = when (logSize) {
                CrashLogSize.SMALL -> "App crashed: ${throwable.message ?: ""}"
                CrashLogSize.MEDIUM -> buildString {
                    append("App crashed: ${throwable.message ?: ""}")
                    append("Thread: ${thread.name}")
                    append("Top Stack Trace: ${throwable.stackTrace.firstOrNull()}")
                }
                CrashLogSize.LARGE -> "App crashed in thread: ${thread.name}\n" + Log.getStackTraceString(throwable)
            }

            log(LogLevel.ERROR, "CRASH", crashMessage, null, skipLogcat = false)

            previousHandler?.uncaughtException(thread, throwable) ?: run {
                android.os.Process.killProcess(android.os.Process.myPid())
                exitProcess(0)
            }
        }
    }

    /**
     * Core logging function that all public-facing log functions call.
     */
    private fun log(
        level: LogLevel,
        tag: String?,
        message: String,
        throwable: Throwable?,
        skipLogcat: Boolean = false
    ) {
        // Check if logging is enabled globally and if the level is sufficient
        if (!config.enabled || level < config.minLevel) {
            return
        }

        val finalTag = "${config.globalTagPrefix}/${tag ?: "Default"}"
        val timestamp = System.currentTimeMillis()
        val threadName = Thread.currentThread().name

        val logMessage = LogMessage(
            level = level,
            tag = finalTag,
            message = message,
            throwable = throwable,
            timestamp = timestamp,
            threadName = threadName
        )

        val formattedMessage = format(logMessage)

        config.destinations.forEach { destination ->
            if (skipLogcat && destination is LogcatDestination)
                return@forEach
            try {
                destination.send(logMessage, formattedMessage)
            } catch (e: Exception) {
                // Log errors in the destination itself to Logcat as a fallback
                Log.e("FlexLogger", "Error in log destination: ${destination.id}", e)
            }
        }
    }

    /**
     * Dynamically formats a LogMessage based on the 'formatOrder' list in the config.
     * Symbols around each field are customizable via the LoggerConfig.symbols field.
     */
    private fun format(log: LogMessage): String {
        val symbols = config.symbols

        return buildString {
            config.formatOrder.forEach { field ->
                when (field) {
                    LogField.TIMESTAMP -> if (config.showTimestamp) {
                        append(symbols.timestampPrefix)
                        append(dateFormatter.format(Date(log.timestamp)))
                        append(symbols.timestampSuffix)
                    }

                    LogField.LEVEL -> {
                        append(symbols.levelPrefix)
                        append(log.level.name.first())
                        append(symbols.levelSuffix)
                    }

                    LogField.TAG -> {
                        append(symbols.tagPrefix)
                        append(log.tag)
                        append(symbols.tagSuffix)
                    }

                    LogField.THREAD -> if (config.showThreadInfo) {
                        append(symbols.threadPrefix)
                        append(log.threadName)
                        append(symbols.threadSuffix)
                    }

                    LogField.MESSAGE -> {
                        append(symbols.messagePrefix)
                        append(log.message)
                        append(symbols.messageSuffix)
                    }
                }
            }
        }
    }

    // Public logging functions
    fun v(tag: String? = null, message: String, throwable: Throwable? = null) = log(LogLevel.VERBOSE, tag, message, throwable)

    fun d(tag: String? = null, message: String, throwable: Throwable? = null) = log(LogLevel.DEBUG, tag, message, throwable)

    fun i(tag: String? = null, message: String, throwable: Throwable? = null) = log(LogLevel.INFO, tag, message, throwable)

    fun w(tag: String? = null, message: String, throwable: Throwable? = null) = log(LogLevel.WARN, tag, message, throwable)

    fun e(tag: String? = null, message: String, throwable: Throwable? = null) = log(LogLevel.ERROR, tag, message, throwable)

    fun wtf(tag: String? = null, message: String, throwable: Throwable? = null) = log(LogLevel.ASSERT, tag, message, throwable)

    /**
     * Logs a JSON string in a pretty-printed format.
     */
    fun json(tag: String? = null, level: LogLevel = LogLevel.DEBUG, jsonString: String?) {
        if (jsonString.isNullOrBlank()) {
            log(level, tag, "Received null or empty JSON string.", null)
            return
        }
        try {
            val prettyJson = when {
                jsonString.startsWith("{") -> JSONObject(jsonString).toString(JSON_INDENT)
                jsonString.startsWith("[") -> JSONArray(jsonString).toString(JSON_INDENT)
                else -> "Invalid JSON format: $jsonString"
            }
            log(level, tag, prettyJson, null)
        } catch (e: JSONException) {
            log(LogLevel.ERROR, tag, "Failed to parse JSON string.", e)
            log(level, tag, jsonString, null) // Log the original string on failure
        }
    }

    /**
     * Logs an XML string in a pretty-printed format.
     */
    fun xml(tag: String? = null, level: LogLevel = LogLevel.DEBUG, xmlString: String?) {
        if (xmlString.isNullOrBlank()) {
            log(level, tag, "Received null or empty XML string.", null)
            return
        }
        try {
            val transformer = TransformerFactory.newInstance().newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT, "yes")
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
            val writer = StringWriter()
            transformer.transform(StreamSource(StringReader(xmlString)), StreamResult(writer))
            val prettyXml = writer.toString()
            log(level, tag, prettyXml, null)
        } catch (e: Exception) {
            log(LogLevel.ERROR, tag, "Failed to parse XML string.", e)
            log(level, tag, xmlString, null) // Log the original string on failure
        }
    }
}