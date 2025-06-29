package com.goodluck3301.flexlogger.log

import android.util.Log
import java.io.File
import java.io.FileWriter

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
                FileWriter(logFile, true).use { writer ->
                    writer.append(formattedMessage)
                    logMessage.throwable?.let {
                        writer.append("\n")
                        writer.append(Log.getStackTraceString(it))
                    }
                    writer.append("\n") // Newline after each log entry
                }

                // After appending, check if the file size exceeds the limit and trim oldest logs.
                if (logFile.exists() && logFile.length() > maxFileSizeBytes) {
                    val allLines = logFile.readLines()
                    val linesToKeep = mutableListOf<String>()
                    var currentSize = 0L

                    // Iterate from the end of the file to keep the newest lines.
                    // Estimate size using byte length of UTF-8 string plus 1 for newline.
                    for (i in allLines.indices.reversed()) {
                        val line = allLines[i]
                        val lineLength = line.toByteArray(Charsets.UTF_8).size + 1 // +1 for newline character

                        // If adding this line would exceed the max size, stop.
                        // All remaining preceding lines are considered too old.
                        if (currentSize + lineLength <= maxFileSizeBytes) {
                            linesToKeep.add(0, line) // Add to the beginning to maintain original order
                            currentSize += lineLength
                        } else {
                            break
                        }
                    }

                    // If we actually trimmed some lines (i.e., not all lines were kept)
                    if (linesToKeep.size < allLines.size) {
                        FileWriter(logFile, false).use { writer -> // Overwrite the file with trimmed content
                            writer.write(linesToKeep.joinToString("\n"))
                            if (linesToKeep.isNotEmpty()) {
                                writer.append("\n") // Ensure a final newline if content exists
                            }
                        }
                        // Log a message to Logcat about the file trimming, not to the file itself
                        // to avoid potential infinite loops if the trimming message itself pushes over the limit.
                        // println("Log file '${logFile.name}' trimmed. Kept ${linesToKeep.size} of ${allLines.size} lines.")
                    } else Unit
                } else Unit
            } catch (e: Exception) {
                // Fallback to Logcat if file writing or management fails for any reason.
                // println("Failed to write or manage log file: ${logFile.absolutePath}")
            }
        }
    }
}