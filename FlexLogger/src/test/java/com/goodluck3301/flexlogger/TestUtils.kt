package com.goodluck3301.flexlogger

import com.goodluck3301.flexlogger.log.FileDestination
import java.io.File

object TestUtils {
    fun getLogFile(fileDestination: FileDestination): File {
        val field = fileDestination.javaClass.superclass!!.getDeclaredField("logFile")
        field.isAccessible = true
        return field.get(fileDestination) as File
    }

    fun getMaxFileSizeMb(fileDestination: FileDestination): Int {
        val field = fileDestination.javaClass.superclass!!.getDeclaredField("maxFileSizeMb")
        field.isAccessible = true
        return field.getInt(fileDestination)
    }
}