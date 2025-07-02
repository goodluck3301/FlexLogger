package com.goodluck3301.flexlogger.log

@Deprecated(
    message = "This function is deprecated. Use log_v() instead.",
    replaceWith = ReplaceWith("log_v(tag, message())")
)
fun flexLogV(tag: String? = null, message: () -> String) = FlexLogger.v(tag, message())

@Deprecated(
    message = "This function is deprecated. Use log_d() instead.",
    replaceWith = ReplaceWith("log_d(tag, message())")
)
fun flexLogD(tag: String? = null, message: () -> String) = FlexLogger.d(tag, message())

@Deprecated(
    message = "This function is deprecated. Use log_i() instead.",
    replaceWith = ReplaceWith("log_i(tag, message())")
)
fun flexLogI(tag: String? = null, message: () -> String) = FlexLogger.i(tag, message())

@Deprecated(
    message = "This function is deprecated. Use log_w() instead.",
    replaceWith = ReplaceWith("log_w(tag, message())")
)
fun flexLogW(tag: String? = null, message: () -> String) = FlexLogger.w(tag, message())

@Deprecated(
    message = "This function is deprecated. Use log_e() instead.",
    replaceWith = ReplaceWith("log_e(tag, throwable, message())")
)
fun flexLogE(tag: String? = null, throwable: Throwable? = null, message: () -> String) = FlexLogger.e(tag, message(), throwable)

@Deprecated(
    message = "This function is deprecated. Use log_wtf() instead.",
    replaceWith = ReplaceWith("log_wtf(tag, throwable, message())")
)
fun flexLogWtf(tag: String? = null, throwable: Throwable? = null, message: () -> String) = FlexLogger.wtf(tag, message(), throwable)

@Deprecated(
    message = "This function is deprecated. Use log_json() instead.",
    replaceWith = ReplaceWith("log_json(tag, level, json())")
)
fun flexLogJson(tag: String? = null, level: LogLevel = LogLevel.DEBUG, json: () -> String?) = FlexLogger.json(tag, level, json())

@Deprecated(
    message = "This function is deprecated. Use log_xml() instead.",
    replaceWith = ReplaceWith("log_xml(tag, level, xml())")
)
fun flexLogXml(tag: String? = null, level: LogLevel = LogLevel.DEBUG, xml: () -> String?) = FlexLogger.xml(tag, level, xml())
