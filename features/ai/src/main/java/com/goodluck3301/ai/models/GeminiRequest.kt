package com.goodluck3301.ai.models

import com.goodluck3301.ai.network.NetworkConstants.PROMPT
import kotlinx.serialization.*

@Serializable
data class GeminiMessageRequestBody(
    val contents: List<GeminiMessage>,
    @SerialName("system_instruction")
    val systemInstruction: GeminiMessage? = null,
)

@Serializable
data class GeminiMessage(
    val parts: List<GeminiMessagePart>,
    val role: String = "user"
)

@Serializable
data class GeminiMessagePart(
    val text: String
)

fun String.toGeminiRequestBody(language: String) = GeminiMessageRequestBody(
    contents = listOf(
        GeminiMessage(
            listOf(
                GeminiMessagePart(PROMPT + "Provide me message in $language language" + this)
            )
        )
    )
)