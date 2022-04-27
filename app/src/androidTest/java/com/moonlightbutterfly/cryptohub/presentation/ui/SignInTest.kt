package com.moonlightbutterfly.cryptohub.presentation.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import junit.framework.TestCase
import org.junit.Test

class SignInTest : CryptoHubAndroidTest() {

    @Test
    fun testSignInPanelVisible() {
        composeTestRule.apply {
            onNodeWithText("Continue without sign in").assertIsDisplayed()
            onNodeWithText("or").assertIsDisplayed()
            onNodeWithText("Sign in with Google").assertIsDisplayed()
        }
        val route = navController.currentBackStackEntry?.destination?.route
        TestCase.assertEquals(route, "sign_in")
    }
}
