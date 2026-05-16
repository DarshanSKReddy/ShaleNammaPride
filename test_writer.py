# Write MainViewModel.kt
with open('app/src/main/java/com/example/shale_nammapride/view/MainViewModel.kt', 'w') as f:
    f.write("""package com.example.shale_nammapride.view

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
    private val repository: FirebaseContentRepository
) : ViewModel() {

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage

    fun submitFeedback(message: String, anonymous: Boolean) {
        viewModelScope.launch {
            repository.submitFeedback(message, anonymous) { success, resultMsg ->
                _statusMessage.value = if (success) "Success: $resultMsg" else "Error: $resultMsg"
            }
        }
    }
    
    fun clearStatus() {
        _statusMessage.value = null
    }
}
""")

# Write MainViewModelTest.kt
with open('app/src/test/java/com/example/shale_nammapride/MainViewModelTest.kt', 'w') as f:
    f.write("""package com.example.shale_nammapride

import com.example.shale_nammapride.data.FirebaseContentRepository
import com.example.shale_nammapride.view.MainViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private val repository: FirebaseContentRepository = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `submitFeedback updates status to success when repo succeeds`() = runTest {
        val callbackSlot = slot<(Boolean, String) -> Unit>()
        every { repository.submitFeedback(any(), any(), capture(callbackSlot)) } answers {
            callbackSlot.captured.invoke(true, "Feedback sent successfully")
        }

        viewModel.submitFeedback("Test feedback", false)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Success: Feedback sent successfully", viewModel.statusMessage.value)
        verify { repository.submitFeedback("Test feedback", false, any()) }
    }
    
    @Test
    fun `clearStatus resets statusMessage to null`() {
        viewModel.clearStatus()
        assertEquals(null, viewModel.statusMessage.value)
    }
}
""")
