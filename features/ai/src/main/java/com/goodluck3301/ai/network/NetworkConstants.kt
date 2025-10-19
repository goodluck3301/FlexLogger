package com.goodluck3301.ai.network

object NetworkConstants {

    const val GEMINI_MESSAGE_USER_TYPE = "user"
    const val GEMINI_MESSAGE_MODEL_TYPE = "model"
    const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta"

    const val PROMPT = "You are an experienced Android developer. " +
            "Explain only the cause of this exception and nothing else. " +
            "Do not provide solutions, suggestions, or extra text. " +
            "Keep all technical terms in English regardless of the selected language."
}