package com.goodluck3301.flexlogger.log

fun log_v(tag: String? = null, message: String) = FlexLogger.v(tag, message)
fun log_d(tag: String? = null, message: String) = FlexLogger.d(tag, message)
fun log_i(tag: String? = null, message: String) = FlexLogger.i(tag, message)
fun log_w(tag: String? = null, message: String) = FlexLogger.w(tag, message)
fun log_e(tag: String? = null, throwable: Throwable? = null, message: String) = FlexLogger.e(tag, message, throwable)
fun log_wtf(tag: String? = null, throwable: Throwable? = null, message: String) = FlexLogger.wtf(tag, message, throwable)
fun log_json(tag: String? = null, level: LogLevel = LogLevel.DEBUG, json: String?) = FlexLogger.json(tag, level, json)
fun log_xml(tag: String? = null, level: LogLevel = LogLevel.DEBUG, xml: String?) = FlexLogger.xml(tag, level, xml)