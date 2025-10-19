package com.goodluck3301.ai.util

fun isInternetReachable(): Boolean {
    repeat(3) { attempt ->
        try {
            val process = Runtime.getRuntime().exec("/system/bin/ping -c 1 www.google.com")
            val returnVal = process.waitFor()
            if (returnVal == 0) {
                return true
            }
        } catch (e: Exception) { }
    }
    return false
}
