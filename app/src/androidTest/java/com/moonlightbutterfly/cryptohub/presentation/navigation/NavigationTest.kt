package com.moonlightbutterfly.cryptohub.presentation.navigation

import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import androidx.navigation.findNavController
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.presentation.core.CryptoHubAndroidTest
import com.moonlightbutterfly.cryptohub.string
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert
import org.junit.Test

@OptIn(ExperimentalCoilApi::class)
@HiltAndroidTest
class NavigationTest: CryptoHubAndroidTest() {

    @Test
    fun testStartingPoint() {
        Assert.assertEquals(getNavController().currentDestination?.id, Screen.CRYPTO_ASSETS_LIST.destinationId)
    }

    @Test
    fun testNavigationToCryptoList() {
        composeTestRule.apply {
            onNodeWithText(string(R.string.crypto_assets_list)).performClick()
            Assert.assertEquals(getNavController().currentDestination?.id, Screen.CRYPTO_ASSETS_LIST.destinationId)
        }
    }

    @Test
    fun testNavigationToSettings() {
        composeTestRule.apply {
            onNodeWithText(string(R.string.settings)).performClick()
            Assert.assertEquals(getNavController().currentDestination?.id, Screen.SETTINGS.destinationId)
        }
    }

    @Test
    fun testNavigationToPanel() {
        composeTestRule.apply {
            onNodeWithText("Bitcoin").performClick()
            Assert.assertEquals(getNavController().currentDestination?.id, Screen.CRYPTO_ASSET_PANEL.destinationId)
        }
    }

    @Test
    fun testNavigationToSearch() {
        composeTestRule.apply {
            onNodeWithContentDescription(string(R.string.search)).performClick()
            Assert.assertEquals(getNavController().currentDestination?.id, Screen.SEARCH_PANEL.destinationId)
        }
    }

    private fun getNavController(): NavController {
        return composeTestRule.activity.findNavController(R.id.nav_host_fragment)
    }
}