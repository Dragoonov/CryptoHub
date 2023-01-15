package com.moonlightbutterfly.cryptohub.presentation.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.moonlightbutterfly.cryptohub.presentation.core.CryptoHubAndroidTest
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import org.junit.Test

@HiltAndroidTest
class CryptoHubNavHostTest : CryptoHubAndroidTest() {

    @Test
    fun testBottomNavigationIsDisplayed() {
        composeTestRule.apply {
            onNodeWithText("Continue without sign in").performClick()
            onNodeWithText("Settings").assertIsDisplayed()
            onNodeWithText("Crypto assets list").assertIsDisplayed()
        }
    }

    @Test
    fun testCryptoListIsFirstDisplayed() {
        composeTestRule.apply {
            onNodeWithText("Continue without sign in").performClick()
            onNodeWithText("Bitcoin").assertIsDisplayed()
            onNodeWithText("Ethereum").assertIsDisplayed()
        }
        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, "cryptoAssetsList")
    }

    @Test
    fun testNavigateToSettings() {
        composeTestRule.apply {
            onNodeWithText("Continue without sign in").performClick()
            onNodeWithText("Settings").performClick()
            onNodeWithText("Device specific").assertIsDisplayed()
        }
        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, "settings")
    }
}
