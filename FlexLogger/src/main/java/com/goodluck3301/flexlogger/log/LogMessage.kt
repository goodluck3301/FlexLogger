package com.goodluck3301.flexlogger.log

/**
 * Represents a single log entry.
 *
 * @property level The severity level of the log.
 * @property tag The tag associated with the log, typically identifying the source.
 * @property message The main content of the log message.
 * @property throwable An optional throwable (e.g., exception) to log.
 * @property timestamp The time the log was created, in milliseconds.
 * @property threadName The name of the thread where the log was issued.
 */
data class LogMessage(
    val level: LogLevel,
    val tag: String,
    val message: String,
    val throwable: Throwable?,
    val timestamp: Long,
    val threadName: String
)
