package com.example.shale_nammapride.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shale_nammapride.data.FirebaseContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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
}
