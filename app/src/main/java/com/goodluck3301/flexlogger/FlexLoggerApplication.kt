package com.goodluck3301.flexlogger

import android.app.Application
import com.goodluck3301.flexlogger.log.FlexLogger
import com.goodluck3301.flexlogger.log.LogLevel
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

            // Add Logcat destination
            logcat()

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
        }
    }
}