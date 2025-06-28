package com.goodluck3301.flexlogger

import com.goodluck3301.flexlogger.log.*
import org.json.JSONObject
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.contains
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class FlexLoggerTest {

    private lateinit var mockDestination: LogDestination

    @Before
    fun setUp() {
        mockDestination = mock()
        FlexLogger.init {
            enabled = true
            minLevel = LogLevel.VERBOSE
            globalTagPrefix = "TestApp"
            showThreadInfo = false
            showTimestamp = false

            addDestination(mockDestination)
        }
    }

    @After
    fun tearDown() {
        FlexLogger.init { } // reset
    }

    @Test
    fun `log verbose message`() {
        FlexLogger.v("TestTag", "This is a verbose message")

        val logCaptor = argumentCaptor<LogMessage>()
        val msgCaptor = argumentCaptor<String>()

        verify(mockDestination).send(logCaptor.capture(), msgCaptor.capture())

        assert(logCaptor.firstValue.message == "This is a verbose message")
        assert(logCaptor.firstValue.tag.contains("TestTag"))
    }

    @Test
    fun `log error with exception`() {
        val exception = RuntimeException("Crash")
        FlexLogger.e("CrashTag", "An error occurred", exception)

        val logCaptor = argumentCaptor<LogMessage>()

        verify(mockDestination).send(logCaptor.capture(), any())

        val capturedLog = logCaptor.firstValue
        assert(capturedLog.message == "An error occurred")
        assert(capturedLog.throwable === exception)
        assert(capturedLog.tag.contains("CrashTag"))
    }

    @Test
    fun `log should ignore below minLevel`() {
        FlexLogger.init {
            enabled = true
            minLevel = LogLevel.ERROR

            addDestination(mockDestination)
        }
        FlexLogger.i("InfoTag", "This should not be logged")
        verify(mockDestination, never()).send(any(), any())
    }

    @Test
    fun `log valid JSON with mock`() {
        val jsonMock = mock<JSONObject>()
        whenever(jsonMock.toString(2)).thenReturn("{\n  \"name\": \"Levon\"\n}")
    }

    @Test
    fun `log valid XML`() {
        val xml = "<note><to>User</to><from>Bot</from></note>"
        FlexLogger.xml("XmlTag", LogLevel.INFO, xml)
        verify(mockDestination).send(any(), contains("<note>"))
    }

    @Test
    fun `log invalid XML`() {
        val badXml = "<note><to>Missing end"
        FlexLogger.xml("XmlTag", LogLevel.INFO, badXml)
        verify(mockDestination, atLeastOnce()).send(any(), contains("Failed to parse XML"))
    }
}
