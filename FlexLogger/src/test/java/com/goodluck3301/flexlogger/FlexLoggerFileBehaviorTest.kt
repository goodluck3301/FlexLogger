package com.goodluck3301.flexlogger

import com.goodluck3301.flexlogger.log.FlexLogger
import com.goodluck3301.flexlogger.log.LogDestination
import com.goodluck3301.flexlogger.log.LogLevel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FlexLoggerFileBehaviorTest {

    private lateinit var mockDestination: LogDestination
    private lateinit var tempLogFile: File
    private val logDir = File("build/tmp/logs")

    @Before
    fun setUp() {
        mockDestination = mock()
    }

    @After
    fun tearDown() {
        FlexLogger.init { }

        if (logDir.exists() && logDir.isDirectory) {
            logDir.listFiles()?.forEach { file ->
                val deleted = file.delete()
                println("Deleted ${file.name}: $deleted")
            }
        }
    }

    @Test
    fun `log any text to file`() {
        if (!logDir.exists()) logDir.mkdirs()

        tempLogFile = File(logDir, "flexlogger_test_log.txt")
        tempLogFile.writeText("")

        FlexLogger.init {
            enabled = true
            minLevel = LogLevel.VERBOSE
            globalTagPrefix = "TestApp"
            showThreadInfo = true
            showTimestamp = true

            file(tempLogFile, maxFileSizeMb = 1)

            addDestination(mockDestination)
        }

        val message = "Hello FlexLogger!"

        FlexLogger.d("TestTag", message)

        val content = tempLogFile.readText()
        // println("File content:\n$content")

        assertTrue(content.contains(message), "Log file should contain the message.")
        assertTrue(content.contains("TestTag"), "Log file should contain the tag.")
    }

    @Test
    fun `if the log file size exceeds 1MB, delete old logs and append new logs`() = runBlocking {
        if (!logDir.exists()) logDir.mkdirs()

        val tempLogFile = File(logDir, "1mb_flexlogger_test_log.txt")
        tempLogFile.writeText("")

        val resourceUrl = javaClass.classLoader?.getResource("test_1mb_file.txt")
        requireNotNull(resourceUrl) { "Resource file not found" }

        val originalFile = File(resourceUrl.toURI())
        tempLogFile.writeBytes(originalFile.readBytes())

        val sizeInKilobytes =  tempLogFile.length() / 1024

        val newMessage = "New message"

        FlexLogger.init {
            enabled = true
            minLevel = LogLevel.VERBOSE
            globalTagPrefix = "TestApp"
            showThreadInfo = true
            showTimestamp = true
            file(tempLogFile, maxFileSizeMb = 1)
            addDestination(mockDestination)
        }

        val newLogCount = 1000

        repeat(newLogCount) {
            FlexLogger.d("NewTag", newMessage)
        }

        val finalFileSizeInKilobytes = tempLogFile.length() / 1024

        val fileContent = tempLogFile.readText()
        val count = Regex(Regex.escape(newMessage)).findAll(fileContent).count()

        assertTrue(fileContent.contains(newMessage))
        assertEquals(count, newLogCount)
        assertEquals(sizeInKilobytes, finalFileSizeInKilobytes)
    }
}