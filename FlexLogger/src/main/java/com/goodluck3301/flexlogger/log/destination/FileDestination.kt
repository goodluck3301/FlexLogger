package com.goodluck3301.flexlogger.log.destination

import android.content.Context
import com.goodluck3301.flexlogger.log.base.BaseFileDestination
import java.io.File

/**
 * A LogDestination that writes logs to a file in the app's cache directory.
 *
 * @param context The application context.
 * @param fileName The name of the log file.
 * @param directory The subdirectory within the cache dir to store logs.
 * @param maxFileSizeMb The maximum file size in megabytes.
 */
class FileDestination(
    context: Context,
    fileName: String,
    directory: String,
    maxFileSizeMb: Int
) : BaseFileDestination(
    logFile = File(File(context.cacheDir, directory), fileName),
    maxFileSizeMb = maxFileSizeMb
) {
    override val id: String = "flexlogger.file.$fileName"
}