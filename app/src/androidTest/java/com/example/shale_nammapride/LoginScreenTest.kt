package com.example.shale_nammapride

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.shale_nammapride.view.LoginScreen
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_initialState_showsLoginComponents() {
        composeTestRule.setContent {
            LoginScreen(onLoginSuccess = {})
        }

        // Verify title
        composeTestRule.onNodeWithText("Welcome Back", substring = true, ignoreCase = true).assertExists()
        
        // Verify text fields
        composeTestRule.onNodeWithText("Email").assertExists()
        composeTestRule.onNodeWithText("Password").assertExists()
        
        // Verify login button exists
        composeTestRule.onNodeWithText("Login").assertExists()
    }
}
