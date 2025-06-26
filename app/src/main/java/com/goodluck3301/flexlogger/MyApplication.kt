package com.goodluck3301.flexlogger

import android.app.Application
import com.goodluck3301.flexlogger.log.FlexLogger
import com.goodluck3301.flexlogger.log.LogLevel

class FlexLoggerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize FlexLogger using the Kotlin DSL
        FlexLogger.init {
            enabled = true // Enable all logging for this example
            globalTagPrefix = "ComposeApp" // Custom tag prefix
            minLevel = LogLevel.VERBOSE // Log all levels

            // Add Logcat destination
            logcat()

            // Add a file destination (logs will be saved in app's cache directory)
            file(
                context = this@FlexLoggerApplication,
                fileName = "compose_app_log.txt",
                maxFileSizeMb = 2 // 2MB max file size before trimming
            )
        }
    }
}