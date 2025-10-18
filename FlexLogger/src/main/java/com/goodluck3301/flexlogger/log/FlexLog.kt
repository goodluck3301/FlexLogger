package com.goodluck3301.flexlogger.log

fun log_v(tag: String? = null, message: String) = FlexLogger.v(tag, message)
fun log_d(tag: String? = null, message: String) = FlexLogger.d(tag, message)
fun log_i(tag: String? = null, message: String) = FlexLogger.i(tag, message)
fun log_w(tag: String? = null, message: String) = FlexLogger.w(tag, message)
fun log_e(tag: String? = null, message: String, throwable: Throwable? = null) = FlexLogger.e(tag, message, throwable)
fun log_wtf(tag: String? = null, message: String, throwable: Throwable? = null) = FlexLogger.wtf(tag, message, throwable)
fun log_json(tag: String? = null, json: String?, level: LogLevel = LogLevel.DEBUG) = FlexLogger.json(tag, level, json)
fun log_xml(tag: String? = null, xml: String?, level: LogLevel = LogLevel.DEBUG) = FlexLogger.xml(tag, level, xml)