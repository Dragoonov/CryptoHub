package com.moonlightbutterfly.cryptohub.presentation.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import junit.framework.TestCase
import org.junit.Test

class SignInTest : CryptoHubAndroidTest() {

    @Test
    fun testSignInPanelVisible() {
        composeTestRule.apply {
            onNodeWithText("Sign in with Google").assertIsDisplayed()
            onNodeWithText("Sign in with email").assertIsDisplayed()
            onNodeWithText("Sign in with phone number").assertIsDisplayed()
            onNodeWithText("Sign in with Facebook").assertIsDisplayed()
            onNodeWithText("Sign in with Twitter").assertIsDisplayed()
            onNodeWithText("or").assertIsDisplayed()
            onNodeWithText("Continue without sign in").assertIsDisplayed()
        }
        val route = navController.currentBackStackEntry?.destination?.route
        TestCase.assertEquals(route, "sign_in")
    }

    @Test
    fun testSignInEmail() {
        composeTestRule.apply {
            onNodeWithText("Sign in with email").performClick()
            onNodeWithText("Email").assertIsDisplayed()
            onNodeWithText("Password").assertIsDisplayed()
            onNodeWithText("Continue").performClick()
            onNodeWithText("Email").assertIsDisplayed()
            onNodeWithText("Password").assertIsDisplayed()
        }
    }

    @Test
    fun testSignInPhone() {
        composeTestRule.apply {
            onNodeWithText("Sign in with phone number").performClick()
            onNodeWithText("Phone number").assertIsDisplayed()
            onNodeWithText("Continue").performClick()
            onNodeWithText("Phone number").assertIsDisplayed()
        }
    }
}
