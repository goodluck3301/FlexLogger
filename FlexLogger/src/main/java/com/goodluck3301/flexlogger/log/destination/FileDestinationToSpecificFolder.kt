package com.goodluck3301.flexlogger.log.destination

import com.goodluck3301.flexlogger.log.base.BaseFileDestination
import java.io.File

/**
 * A LogDestination that writes logs to a provided File object.
 *
 * @param logFile The file to write logs to.
 * @param maxFileSizeMb The maximum file size in megabytes.
 */
class FileDestinationToSpecificFolder(
    logFile: File,
    maxFileSizeMb: Int
) : BaseFileDestination(
    logFile = logFile,
    maxFileSizeMb = maxFileSizeMb
) {
    override val id: String = "flexlogger.specifyed.file.${logFile.name}"
}