package com.goodluck3301.app.data.model.gemini

import com.goodluck3301.ai.models.AiMessage
import com.goodluck3301.ai.models.AiMessageType
import com.goodluck3301.ai.models.GeminiMessage
import com.goodluck3301.ai.network.NetworkConstants.GEMINI_MESSAGE_MODEL_TYPE
import com.goodluck3301.ai.network.NetworkConstants.GEMINI_MESSAGE_USER_TYPE
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


data class GeminiResponse(
    val candidates: List<GeminiCandidate>? = null,
    val error: GeminiError? = null
)

data class GeminiCandidate(
    val content: GeminiMessage
)

data class GeminiError(
    val code: Int,
    val message: String
)

@OptIn(ExperimentalTime::class)
fun GeminiResponse.toAiMessage() = AiMessage(
    content = candidates!!.first().content.parts.first().text,
    type = if (candidates.first().content.role == GEMINI_MESSAGE_USER_TYPE) AiMessageType.USER else AiMessageType.MODEL,
    time = Clock.System.now().toEpochMilliseconds()
)

val GeminiResponse.text
    get() = candidates!!.first().content.parts.first().text

val AiMessageType.geminiRole
    get() = if (this == AiMessageType.USER) GEMINI_MESSAGE_USER_TYPE else GEMINI_MESSAGE_MODEL_TYPE