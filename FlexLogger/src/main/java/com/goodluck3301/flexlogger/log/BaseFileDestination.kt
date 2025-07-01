package com.goodluck3301.flexlogger.log

import android.util.Log
import java.io.File
import java.io.FileWriter
import java.util.concurrent.Executors

/**
 * Base class for log destinations that write to a file.
 * It handles the core logic of writing log messages and trimming the file when it exceeds a specified size.
 *
 * @property logFile The file where logs will be written.
 * @property maxFileSizeMb The maximum file size in megabytes before the log is trimmed.
 */
abstract class BaseFileDestination(
    protected val logFile: File,
    private val maxFileSizeMb: Int
) : LogDestination {

    private val maxFileSizeBytes: Long = maxFileSizeMb * 1024L * 1024L
    private val logExecutor = Executors.newSingleThreadExecutor()

    init {
        // Ensure the parent directory for the log file exists.
        logFile.parentFile?.let {
            if (!it.exists()) {
                it.mkdirs()
            }
        }
    }

    override fun send(logMessage: LogMessage, formattedMessage: String) {
        // Synchronize to prevent race conditions from multiple threads trying to write/manage the file.
        synchronized(this) {
            try {
                // Append the new log message first.
                appendLogMessageToFile(logMessage, formattedMessage)

                // After appending, check if the file size exceeds the limit and trim oldest logs.
                if (logFile.exists() && logFile.length() > maxFileSizeBytes) {
                    logExecutor.execute {
                        trimLogFileIfNeeded()
                    }
                }
            } catch (e: Exception) {
                // Fallback to Logcat if file writing or management fails for any reason.
                // println("Failed to write or manage log file: ${logFile.absolutePath}")
            }
        }
    }

    private fun appendLogMessageToFile(logMessage: LogMessage, formattedMessage: String) {
        FileWriter(logFile, true).use { writer ->
            writer.append(formattedMessage)
            logMessage.throwable?.let {
                writer.append("\n")
                writer.append(Log.getStackTraceString(it))
            }
            writer.append("\n") // Newline after each log entry
        }
    }

    private fun trimLogFileIfNeeded() {
        synchronized(this) {
            val allLines = logFile.readLines()
            val linesToKeep = mutableListOf<String>()
            var currentSize = 0L

            // Iterate from the end of the file to keep the newest lines.
            // Estimate size using byte length of UTF-8 string plus 1 for newline.
            for (i in allLines.indices.reversed()) {
                val line = allLines[i]
                val lineSize = line.toByteArray(Charsets.UTF_8).size + 1 // +1 for newline character

                // If adding this line would exceed the max size, stop.
                // All remaining preceding lines are considered too old.
                if (currentSize + lineSize <= maxFileSizeBytes) {
                    linesToKeep.add(0, line) // Add to the beginning to maintain original order
                    currentSize += lineSize
                } else break
            }

            // If we actually trimmed some lines (i.e., not all lines were kept)
            if (linesToKeep.size < allLines.size) {
                // Overwrite the file with trimmed content
                FileWriter(logFile, false).use { writer ->
                    writer.write(linesToKeep.joinToString("\n"))
                    if (linesToKeep.isNotEmpty()) {
                        writer.append("\n") // Ensure a final newline if content exists
                    }
                }
                // Log a message to Logcat about the file trimming, not to the file itself
                // to avoid potential infinite loops if the trimming message itself pushes over the limit.
            }
        }
    }
}