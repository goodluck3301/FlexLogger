package com.goodluck3301.flexlogger

import android.app.Application
import com.goodluck3301.flexlogger.log.enums.CrashLogSize
import com.goodluck3301.flexlogger.log.FlexLogger
import com.goodluck3301.flexlogger.log.enums.LogField
import com.goodluck3301.flexlogger.log.model.LogFormatSymbols
import com.goodluck3301.flexlogger.log.enums.LogLevel
import com.goodluck3301.flexlogger.log.model.AiConfig
import java.io.File

class FlexLoggerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize FlexLogger using the Kotlin DSL
        FlexLogger.init {
            enabled = true // Enable all logging for this example
            globalTagPrefix = "ComposeApp" // Custom tag prefix
            minLevel = LogLevel.VERBOSE // Log all levels
            enableCrashLogging = true // Enable crash logging (default is true)

            // Call removeLogcatDestination() if you want to disable logging in Logcat

            // AI config
            // The AI will automatically process crash exceptions and suggest possible solutions.
            // To use this feature, you need a valid Gemini API key.
            // 👉 How to get your Gemini API key: https://www.youtube.com/watch?v=RVGbLSVFtIk
            aiConfig = AiConfig(
                apiKey = "API_KEY", // Replace with your Gemini API key
                model = "gemini-2.5-flash", // by default 'gemini-2.5-flash'
                languageResponse = "English"
            )

            // Add a file destination (logs will be saved in app's cache directory)
            file(
                context = this@FlexLoggerApplication,
                fileName = "compose_app_log.txt",
                maxFileSizeMb = 2 // 2MB max file size before trimming
            )

            // Add a file destination (logs will be saved in app's internal files directory under packageName/logs)
            file(
                // Create the full log file path:
                // Internal storage → /data/data/<package_name>/files/<package_name>/logs/app_log.txt
                File(
                    this@FlexLoggerApplication.filesDir, // Base directory for app's internal storage
                    "${this@FlexLoggerApplication.packageName}/logs/app_log.txt"  // Subdirectory using package name, then "logs", then file name
                ),
                maxFileSizeMb = 1 // Maximum file size in MB before old log entries are trimmed (1MB here)
            )

            // Custom format order
            formatOrder = listOf(
                LogField.TIMESTAMP,
                LogField.THREAD,
                LogField.LEVEL,
                LogField.TAG,
                LogField.MESSAGE
            )

            // Custom formatting symbols
            symbols = LogFormatSymbols(
                timestampPrefix = "<",
                timestampSuffix = "> ",
                levelPrefix = "(",
                levelSuffix = ") → ",
                tagPrefix = "[TAG: ",
                tagSuffix = "] ",
                tagSeparator = "::",
                threadPrefix = "<T:",
                threadSuffix = "> ",
                messagePrefix = ":: ",
                messageSuffix = " <<END>>"
            )

            crashLogSize = CrashLogSize.MEDIUM
        }
    }
}