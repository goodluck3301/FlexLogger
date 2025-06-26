package com.goodluck3301.flexlogger.log

import android.util.Log

/**
 * A LogDestination that writes log messages to Android's Logcat.
 */
class LogcatDestination : LogDestination {
    override val id: String = "flexlogger.logcat"

    override fun send(logMessage: LogMessage, formattedMessage: String) {
        val fullTag = logMessage.tag
        val fullMessage = if (logMessage.throwable != null) {
            // Logcat handles the throwable separately for stack trace printing.
            logMessage.message
        } else {
            formattedMessage
        }

        when (logMessage.level) {
            LogLevel.VERBOSE -> Log.v(fullTag, fullMessage, logMessage.throwable)
            LogLevel.DEBUG -> Log.d(fullTag, fullMessage, logMessage.throwable)
            LogLevel.INFO -> Log.i(fullTag, fullMessage, logMessage.throwable)
            LogLevel.WARN -> Log.w(fullTag, fullMessage, logMessage.throwable)
            LogLevel.ERROR -> Log.e(fullTag, fullMessage, logMessage.throwable)
            LogLevel.ASSERT -> Log.wtf(fullTag, fullMessage, logMessage.throwable)
        }
    }
}
