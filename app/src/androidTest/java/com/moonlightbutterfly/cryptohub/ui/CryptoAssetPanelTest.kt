package com.moonlightbutterfly.cryptohub.ui

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.di.DaggerTestAppComponent
import com.moonlightbutterfly.cryptohub.ui.composables.AppLayout
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CryptoassetPanelTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var navController: NavHostController

    @ExperimentalCoilApi
    @Before
    fun setupNavHost() {
        composeTestRule.setContent {
            val viewModelFactory = DaggerTestAppComponent.create().viewModelFactory()
            CompositionLocalProvider(LocalViewModelFactory provides viewModelFactory) {
                navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                AppLayout(navController = navController, backStackEntry = backStackEntry)
            }
        }
    }

    @Test
    fun testGoingThroughApp() {
        composeTestRule.apply {
            onNodeWithText("Bitcoin").performClick()
            onNodeWithText("Bitcoin").assertIsDisplayed()
            onNodeWithText("BTC").assertIsDisplayed()
            onNodeWithText("Statistics").assertIsDisplayed()

            onNodeWithText("Cryptoassets list").performClick()
            onNodeWithText("Ethereum").performClick()
            onNodeWithText("Ethereum").assertIsDisplayed()
            onNodeWithText("ETH").assertIsDisplayed()
            onNodeWithText("Statistics").assertIsDisplayed()
        }
    }
}
