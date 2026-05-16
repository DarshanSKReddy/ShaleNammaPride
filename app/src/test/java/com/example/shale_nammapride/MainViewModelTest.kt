package com.example.shale_nammapride

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
