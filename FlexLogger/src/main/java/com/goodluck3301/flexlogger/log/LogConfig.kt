package com.goodluck3301.flexlogger.log

import android.content.Context

/**
 * Holds the configuration for the FlexLogger instance.
 * This class is designed to be configured using a Kotlin DSL.
 */
data class LogConfig(
    var enabled: Boolean = true,
    var globalTagPrefix: String = "App",
    var minLevel: LogLevel = LogLevel.VERBOSE,
    var showTimestamp: Boolean = true,
    var showThreadInfo: Boolean = true,
    var timestampFormat: String = "yyyy-MM-dd HH:mm:ss.SSS",
    internal val destinations: MutableSet<LogDestination> = mutableSetOf()
) {
    /**
     * Adds a LogDestination to the configuration.
     * @param destination The destination to add.
     */
    fun addDestination(destination: LogDestination) {
        destinations.add(destination)
    }

    /**
     * Convenience function to add the default Logcat destination.
     */
    fun logcat() {
        addDestination(LogcatDestination())
    }

    /**
     * Convenience function to add a file logging destination with size-based rotation.
     * @param context The application context, needed to resolve the file path.
     * @param fileName The name of the log file.
     * @param directory The directory within the app's cache folder to store the log file.
     * @param maxFileSizeMb The maximum size in megabytes before the log file is cleared.
     */
    fun file(
        context: Context,
        fileName: String = "app_log.txt",
        directory: String = "logs",
        maxFileSizeMb: Int = 5
    ) {
        addDestination(FileDestination(context, fileName, directory, maxFileSizeMb))
    }
}