package com.goodluck3301.ai.models

data class AiMessage(
    val content: String,
    val type: AiMessageType,
    val time: Long,
    val attachmentsText: String = ""
)

enum class AiMessageType {
    USER,
    MODEL
}
