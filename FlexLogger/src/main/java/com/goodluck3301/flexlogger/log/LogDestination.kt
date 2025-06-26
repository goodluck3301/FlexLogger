package com.goodluck3301.flexlogger.log

/**
 * Defines a destination for log messages.
 * Implement this interface to send logs to custom backends like a remote server,
 * analytics service, or a different local storage mechanism.
 */
interface LogDestination {
    /**
     * Sends the log message to this destination.
     * @param logMessage The complete log message object.
     * @param formattedMessage The pre-formatted, human-readable string representation of the log.
     */
    fun send(logMessage: LogMessage, formattedMessage: String)

    /**
     * A unique identifier for the destination. Used for removal.
     */
    val id: String
}