package com.goodluck3301.ai.api

import com.goodluck3301.ai.models.toGeminiRequestBody
import com.goodluck3301.ai.network.GeminiRetrofitInstance
import com.goodluck3301.ai.network.NetworkConstants.BASE_URL
import com.goodluck3301.ai.util.NetworkResult
import com.goodluck3301.ai.util.isInternetReachable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiApi {

    suspend fun sendMessage(
        prompt: String,
        model: String,
        key: String,
        language: String
    ): NetworkResult<String?> {
        return withContext(Dispatchers.IO) {
            try {
                if (!isInternetReachable()) return@withContext NetworkResult.InternetError

                val endpoint = "$BASE_URL/models/$model:generateContent"
                val body = prompt.toGeminiRequestBody(language)

                val response = GeminiRetrofitInstance.api.sendMessage(endpoint, key, body)
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data?.error != null) {
                        handleError(data.error.message, data.error.code)
                    } else {
                        return@withContext NetworkResult.Success(data?.candidates?.get(0)?.content?.parts[0]?.text)
                    }
                } else {
                    return@withContext NetworkResult.OtherError("HTTP ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext NetworkResult.OtherError(e.message ?: "Unknown error")
            }
        }
    }

    private fun handleError(message: String, code: Int?): NetworkResult<String> {
        return when {
            message.contains("API key", ignoreCase = true) -> NetworkResult.InvalidKey
            code in 400..499 -> NetworkResult.OtherError(message)
            else -> NetworkResult.OtherError()
        }
    }
}