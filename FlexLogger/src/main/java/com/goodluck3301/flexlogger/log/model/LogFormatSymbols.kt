package com.goodluck3301.flexlogger.log.model

/**
 * Defines all customizable symbols used to format a log message.
 * Each field has optional prefix and suffix for full control.
 */
data class LogFormatSymbols(
    val timestampPrefix: String = "",
    val timestampSuffix: String = " ",

    val levelPrefix: String = "",
    val levelSuffix: String = "/",

    val tagPrefix: String = "",
    val tagSuffix: String = "",

    val threadPrefix: String = "[",
    val threadSuffix: String = "]",

    val messagePrefix: String = ": ",
    val messageSuffix: String = "",

    val tagSeparator: String = "/"
)