package com.goodluck3301.flexlogger

import android.content.Context
import com.goodluck3301.flexlogger.helpers.TestUtils.getLogFile
import com.goodluck3301.flexlogger.helpers.TestUtils.getMaxFileSizeMb
import com.goodluck3301.flexlogger.log.FileDestination
import com.goodluck3301.flexlogger.log.LogLevel
import com.goodluck3301.flexlogger.log.LogMessage
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.io.File

class FileDestinationTest {

    private lateinit var context: Context
    private val logDir: File = File("build/tmp/")

    private val directory = "logs"
    private val fileName = "app.log"
    private val maxFileSizeMb = 5


    @Before
    fun setUp() {
        context = mock(Context::class.java)
        `when`(context.cacheDir).thenReturn(logDir)
    }

    @After
    fun tearDown() {
        if (logDir.exists() && logDir.isDirectory) {
            logDir.listFiles()?.forEach { file ->
                val deleted = file.delete()
                println("Deleted ${file.name}: $deleted")
            }
        }
    }

    @Test
    fun `log file should be created in correct path`() {
        val fileDestination = FileDestination(context, fileName, directory, maxFileSizeMb)
        val logFile = getLogFile(fileDestination)

        val expectedFile = File(File(logDir, directory), fileName)

        assertEquals(expectedFile.absolutePath, logFile.absolutePath)
        logFile.parentFile?.let { assertTrue(it.exists()) }
    }

    @Test
    fun `id should be formatted correctly`() {
        val fileDestination = FileDestination(context, fileName, directory, maxFileSizeMb)

        val expectedId = "flexlogger.file.$fileName"
        assertEquals(expectedId, fileDestination.id)
    }

    @Test
    fun `max file size should be set correctly`() {
        val fileDestination = FileDestination(context, fileName, directory, maxFileSizeMb)
        val actualSize = getMaxFileSizeMb(fileDestination)

        assertEquals(maxFileSizeMb, actualSize)
    }

    @Test
    fun `send should not crash and should keep file available`() {
        val fileDestination = FileDestination(context, fileName, directory, maxFileSizeMb)

        val logMessage = LogMessage(
            level = LogLevel.INFO,
            tag = "TestTag",
            message = "Test message",
            threadName = "main",
            throwable = null,
            timestamp = 1751285783L
        )

        val formattedMessage = "INFO/TestTag: Test message"

        try {
            fileDestination.send(logMessage, formattedMessage)
            val logFile = getLogFile(fileDestination)
            assertTrue(logFile.exists())
        } catch (e: Exception) {
            fail("send() threw an exception: ${e.message}")
        }
    }
}
