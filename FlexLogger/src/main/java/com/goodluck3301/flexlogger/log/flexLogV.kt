package com.goodluck3301.flexlogger.log

fun flexLogV(tag: String? = null, message: () -> String) = FlexLogger.v(tag, message())
fun flexLogD(tag: String? = null, message: () -> String) = FlexLogger.d(tag, message())
fun flexLogI(tag: String? = null, message: () -> String) = FlexLogger.i(tag, message())
fun flexLogW(tag: String? = null, message: () -> String) = FlexLogger.w(tag, message())
fun flexLogE(tag: String? = null, throwable: Throwable? = null, message: () -> String) = FlexLogger.e(tag, message(), throwable)
fun flexLogWtf(tag: String? = null, throwable: Throwable? = null, message: () -> String) = FlexLogger.wtf(tag, message(), throwable)

fun flexLogJson(tag: String? = null, level: LogLevel = LogLevel.DEBUG, json: () -> String?) = FlexLogger.json(tag, level, json())
fun flexLogXml(tag: String? = null, level: LogLevel = LogLevel.DEBUG, xml: () -> String?) = FlexLogger.xml(tag, level, xml())
