package com.goodluck3301.flexlogger.log.model

/**
 * Configuration class for AI requests.
 *
 * @since Tutorial How to get your Gemini API key: https://www.youtube.com/watch?v=RVGbLSVFtIk
 *
 * @property apiKey The API key used to authenticate requests to the AI service.
 * @property model The AI model to use for generating responses (default: "gemini-2.5-flash").
 * @property languageResponse The language in which the AI should provide its responses (default: "English").
 */
data class AiConfig(
    val apiKey: String = "",
    val model: String = "gemini-2.5-flash",
    val languageResponse: String = "English",
)