package com.moonlightbutterfly.cryptohub.database

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.moonlightbutterfly.cryptohub.di.DaggerTestAppComponent
import com.moonlightbutterfly.cryptohub.ui.LocalViewModelFactory
import com.moonlightbutterfly.cryptohub.ui.composables.AppLayout
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CryptoHubNavHostTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    lateinit var navController: NavHostController

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
    fun testBottomNavigationIsDisplayed() {
        composeTestRule.apply {
            onNodeWithText("Settings").assertIsDisplayed()
            onNodeWithText("Cryptocurrencies list").assertIsDisplayed()
        }
    }

    @Test
    fun testCryptoListIsFirstDisplayed() {
        composeTestRule.apply {
            onNodeWithText("Bitcoin").assertIsDisplayed()
            onNodeWithText("Ethereum").assertIsDisplayed()
        }
        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, "cryptocurrenciesList")
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
