package com.goodluck3301.flexlogger.log

data class LogFormatSymbols(
    val levelSeparator: String = "/",     // Between level and tag
    val threadPrefix: String = "[",       // Before thread name
    val threadSuffix: String = "]",       // After thread name
    val messagePrefix: String = ": ",     // Before message text
    val timestampSuffix: String = " "     // After timestamp
)
