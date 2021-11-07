package com.moonlightbutterfly.cryptohub.presentation.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import org.junit.Test

class CryptoAssetPanelTest : CryptoHubAndroidTest() {

    @Test
    fun testGoingThroughApp() {
        composeTestRule.apply {
            onNodeWithText("BTC").performClick()
            onNodeWithText("BTC").assertIsDisplayed()
            onNodeWithText("Statistics").assertIsDisplayed()

            onNodeWithText("Crypto assets list").performClick()
            onNodeWithText("ETH").performClick()
            onNodeWithText("ETH").assertIsDisplayed()
            onNodeWithText("Statistics").assertIsDisplayed()
        }
    }

    @Test
    fun testManagingFavourites() {
        composeTestRule.apply {
            onNodeWithText("Bitcoin").performClick()
            onNodeWithText("BTC").assertIsDisplayed()
            onNodeWithContentDescription("Favourites").performClick()
            Espresso.pressBack()
            onNodeWithText("Favourites").performClick()
            onNodeWithText("BTC").assertIsDisplayed()
            onNodeWithContentDescription("Favourites").performClick()
            onNodeWithText("Crypto assets list").performClick()
            onNodeWithText("Favourites").performClick()
            onNodeWithText("BTC").assertDoesNotExist()
        }
    }
}
