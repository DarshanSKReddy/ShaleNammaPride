package com.example.shale_nammapride.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shale_nammapride.data.FirebaseContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.example.shale_nammapride.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content


@HiltViewModel
class MainViewModel @Inject constructor(
    val repository: FirebaseContentRepository
) : ViewModel() {

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage

    fun submitFeedback(message: String, anonymous: Boolean, onComplete: (Boolean, String) -> Unit = {_,_->}) {
        viewModelScope.launch {
            repository.submitFeedback(message, anonymous) { success, resultMsg ->
                _statusMessage.value = if (success) "Success: $resultMsg" else "Error: $resultMsg"
                onComplete(success, resultMsg)
            }
        }
    }
    
    fun clearStatus() {
        _statusMessage.value = null
    }


    private val _summaryResult = MutableStateFlow<String?>(null)
    val summaryResult: StateFlow<String?> = _summaryResult

    fun generateFeedbackSummary(recentFeedbacks: List<String>) {
        if (recentFeedbacks.isEmpty()) {
            _summaryResult.value = "No feedback available to summarize."
            return
        }

        viewModelScope.launch {
            _summaryResult.value = "Generating summary..."
            try {
                val apiKey = BuildConfig.GEMINI_API_KEY
                if (apiKey.isBlank() || apiKey == "null") {
                    _summaryResult.value = "Error: GEMINI_API_KEY is not configured."
                    return@launch
                }

                val generativeModel = GenerativeModel(
                    modelName = "gemini-1.5-flash",
                    apiKey = apiKey
                )

                val prompt = "You are an AI assistant for a school management app. Here are the latest feedback messages from students and parents. Please provide a concise 'Weekly Insights Summary' highlighting the main themes, areas of praise, and concerns: \n\n" + recentFeedbacks.joinToString("\n- ")

                val response = generativeModel.generateContent(prompt)
                _summaryResult.value = response.text ?: "Error: Received empty response from AI."
            } catch (e: Exception) {
                _summaryResult.value = "Error: ${e.localizedMessage}"
            }
        }
    }
}