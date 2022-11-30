package com.moonlightbutterfly.cryptohub.presentation.signin

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import com.moonlightbutterfly.cryptohub.presentation.core.CryptoHubAndroidTest
import junit.framework.TestCase
import org.junit.Test

class SignInTest : CryptoHubAndroidTest() {

    @Test
    fun testSignInPanelVisible() {
        composeTestRule.apply {
            onNodeWithText("Sign in").assertIsDisplayed()
            onNodeWithText("or").assertIsDisplayed()
            onNodeWithText("Continue without sign in").assertIsDisplayed()
        }
        val route = navController.currentBackStackEntry?.destination?.route
        TestCase.assertEquals(route, "sign_in")
    }
}
