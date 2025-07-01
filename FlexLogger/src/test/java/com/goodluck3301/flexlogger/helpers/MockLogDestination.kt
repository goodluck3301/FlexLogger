package com.goodluck3301.flexlogger.helpers

import com.goodluck3301.flexlogger.log.*
import java.util.Collections.synchronizedList

class MockLogDestination : LogDestination {

    val logs: MutableList<String> = synchronizedList(mutableListOf<String>())
    
    override fun send(logMessage: LogMessage, formattedMessage: String) {
        logs.add("[${logMessage.level}] ${logMessage.tag}: $formattedMessage")
    }

    override val id: String
        get() = "MockLogDestination.id"
}