package com.moonlightbutterfly.cryptohub.presentation.search

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.presentation.core.CryptoHubAndroidTest
import com.moonlightbutterfly.cryptohub.string
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test

@HiltAndroidTest
@OptIn(ExperimentalCoilApi::class)
class SearchTest : CryptoHubAndroidTest() {

    @Test
    fun testOpenSearch() {
        composeTestRule.apply {
            onNodeWithContentDescription(string(R.string.search)).performClick()
            onNodeWithContentDescription("SearchInputField").assertIsDisplayed()
        }
    }

    @Test
    fun testClickOnSearchItemAndSaveToRecents() {
        composeTestRule.apply {
            onNodeWithContentDescription(string(R.string.search)).performClick()
            onNodeWithContentDescription("SearchInputField").performTextInput("Eth")
            runBlocking { delay(2000) }
            onNodeWithText("Ethereum").assertIsDisplayed()
            onNodeWithText("Ethereum").performClick()
            onNodeWithText(string(R.string.statistics)).assertIsDisplayed()
            Espresso.pressBack()
            onNodeWithContentDescription("SearchInputField").performTextInput("")
            onNodeWithText("Ethereum").assertIsDisplayed()
        }
    }
}
