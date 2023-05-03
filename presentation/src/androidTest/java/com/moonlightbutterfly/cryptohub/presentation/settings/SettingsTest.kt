package com.moonlightbutterfly.cryptohub.presentation.settings

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.presentation.R
import com.moonlightbutterfly.cryptohub.presentation.core.CryptoHubAndroidTest
import com.moonlightbutterfly.cryptohub.string
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test

@HiltAndroidTest
@OptIn(ExperimentalCoilApi::class)
class SettingsTest : CryptoHubAndroidTest() {

    @Test
    fun testNavigateToSettings() {
        composeTestRule.apply {
            onNodeWithText(string(R.string.settings)).performClick()
            onNodeWithText(string(R.string.device_specific)).assertIsDisplayed()
            onNodeWithText(string(R.string.night_mode)).assertIsDisplayed()
            onNodeWithText(string(R.string.post_notifications)).assertIsDisplayed()
        }
    }
}
