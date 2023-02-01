package com.moonlightbutterfly.cryptohub.presentation.panel

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.presentation.core.CryptoHubAndroidTest
import com.moonlightbutterfly.cryptohub.string
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test

@HiltAndroidTest
@OptIn(ExperimentalCoilApi::class)
class CryptoAssetPanelTest : CryptoHubAndroidTest() {

    @Test
    fun testGoingThroughApp() {
        composeTestRule.apply {
            onNodeWithText("BTC").performClick()
            onNodeWithText("BTC").assertIsDisplayed()
            onNodeWithText(string(R.string.statistics)).assertIsDisplayed()
            onNodeWithText(string(R.string.crypto_assets_list)).performClick()
            onNodeWithText("ETH").performClick()
            onNodeWithText("ETH").assertIsDisplayed()
            onNodeWithText(string(R.string.statistics)).assertIsDisplayed()
        }
    }

// FLAKY
//    @Test
//    fun testManagingFavourites() {
//        composeTestRule.apply {
//            onNodeWithText("Bitcoin").performClick()
//            onNodeWithText("BTC").assertIsDisplayed()
//            onNodeWithContentDescription("Favourites").performClick()
//            Espresso.pressBack()
//            onNodeWithText("Favourites").performClick()
//            onNodeWithText("BTC").assertIsDisplayed()
//            onNodeWithContentDescription("Favourites").performClick()
//            onNodeWithText("Settings").performClick()
//            onNodeWithText("Crypto assets list").performClick()
//            onNodeWithText("Favourites").performClick()
//            onNodeWithText("BTC").assertDoesNotExist()
//        }
//    }
}
