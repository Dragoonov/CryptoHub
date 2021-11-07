package com.moonlightbutterfly.cryptohub.presentation.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import junit.framework.TestCase.assertEquals
import org.junit.Test

class CryptoHubNavHostTest : CryptoHubAndroidTest() {

    @Test
    fun testBottomNavigationIsDisplayed() {
        composeTestRule.apply {
            onNodeWithText("Settings").assertIsDisplayed()
            onNodeWithText("Crypto assets list").assertIsDisplayed()
        }
    }

    @Test
    fun testCryptoListIsFirstDisplayed() {
        composeTestRule.apply {
            onNodeWithText("Bitcoin").assertIsDisplayed()
            onNodeWithText("Ethereum").assertIsDisplayed()
        }
        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, "cryptoAssetsList")
    }

    @Test
    fun testNavigateToSettings() {
        composeTestRule.apply {
            onNodeWithText("Settings").performClick()
            onNodeWithText("General").assertIsDisplayed()
        }
        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, "settings")
    }
}
