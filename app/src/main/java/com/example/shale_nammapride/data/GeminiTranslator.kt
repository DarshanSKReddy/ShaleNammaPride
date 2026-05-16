package com.example.shale_nammapride.data

import com.example.shale_nammapride.BuildConfig
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

object GeminiTranslator {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val api: GeminiApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiApi::class.java)
    }

    fun isConfigured(): Boolean = BuildConfig.GEMINI_API_KEY.isNotBlank()

    suspend fun translateToKannada(text: String): String? {
        val cleanText = text.trim()
        if (cleanText.isBlank() || !isConfigured()) return null

        val request = GeminiRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(
                            text = "Translate the following school app content from English to Kannada. Return only the Kannada translation and keep names, numbers, and URLs unchanged: $cleanText"
                        )
                    )
                )
            ),
            generationConfig = GenerationConfig(
                temperature = 0.2
            )
        )

        return runCatching {
            val response = api.generateContent(
                apiKey = BuildConfig.GEMINI_API_KEY,
                request = request
            )
            response.candidates
                .firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text
                ?.trim()
                ?.takeIf { it.isNotBlank() }
        }.getOrNull()
    }
}

private interface GeminiApi {
    @POST("v1beta/models/gemini-1.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

private data class GeminiRequest(
    val contents: List<Content>,
    @SerializedName("generationConfig")
    val generationConfig: GenerationConfig? = null
)

private data class GeminiResponse(
    val candidates: List<Candidate> = emptyList()
)

private data class Candidate(
    val content: Content? = null
)

private data class Content(
    val parts: List<Part> = emptyList()
)

private data class Part(
    val text: String = ""
)

private data class GenerationConfig(
    val temperature: Double = 0.2
)
