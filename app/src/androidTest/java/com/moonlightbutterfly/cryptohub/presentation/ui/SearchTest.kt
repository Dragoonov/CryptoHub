package com.moonlightbutterfly.cryptohub.presentation.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import junit.framework.TestCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SearchTest : CryptoHubAndroidTest() {

    @Test
    fun testOpenSearch() {
        composeTestRule.apply {
            onNodeWithText("Continue without sign in").performClick()
            onNodeWithContentDescription("Search").performClick()
        }
        val route = navController.currentBackStackEntry?.destination?.route
        TestCase.assertEquals(route, "search")
    }

    @Test
    fun testClickOnSearchItemAndSaveToRecents() {
        composeTestRule.apply {
            onNodeWithText("Continue without sign in").performClick()
            onNodeWithContentDescription("Search").performClick()
            onNodeWithContentDescription("SearchInputField").performTextInput("Eth")
            runBlocking { delay(2000) }
            onNodeWithText("Ethereum").assertIsDisplayed()
            onNodeWithText("Ethereum").performClick()
            onNodeWithText("Statistics").assertIsDisplayed()
            Espresso.pressBack()
            onNodeWithContentDescription("SearchInputField").performTextInput("")
            onNodeWithText("Ethereum").assertIsDisplayed()
        }
        val route = navController.currentBackStackEntry?.destination?.route
        TestCase.assertEquals(route, "search")
    }
}
