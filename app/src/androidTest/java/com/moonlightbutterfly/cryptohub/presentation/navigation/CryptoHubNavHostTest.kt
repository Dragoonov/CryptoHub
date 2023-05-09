package com.moonlightbutterfly.cryptohub.presentation.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.presentation.R
import com.moonlightbutterfly.cryptohub.presentation.core.CryptoHubAndroidTest
import com.moonlightbutterfly.cryptohub.string
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test

@HiltAndroidTest
@OptIn(ExperimentalCoilApi::class)
class CryptoHubNavHostTest : CryptoHubAndroidTest() {

    @Test
    fun testBottomNavigationIsDisplayed() {
        composeTestRule.apply {
            onNodeWithText(string(R.string.settings)).assertIsDisplayed()
            onNodeWithText(string(R.string.crypto_assets_list)).assertIsDisplayed()
        }
    }

    @Test
    fun testCryptoListIsFirstDisplayed() {
        composeTestRule.apply {
            onNodeWithText("Bitcoin").assertIsDisplayed()
            onNodeWithText("Ethereum").assertIsDisplayed()
        }
    }
}
