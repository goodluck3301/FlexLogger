package com.goodluck3301.ai.network

import com.goodluck3301.ai.models.GeminiMessageRequestBody
import com.goodluck3301.app.data.model.gemini.GeminiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @POST
    suspend fun sendMessage(
        @Url baseUrl: String,
        @Query("key") key: String,
        @Body body: GeminiMessageRequestBody
    ): Response<GeminiResponse>
}