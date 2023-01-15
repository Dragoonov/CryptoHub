package com.moonlightbutterfly.cryptohub.presentation.core

import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.AppLayout
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.FlowPreview
import org.junit.Before
import org.junit.Rule

@HiltAndroidTest
open class CryptoHubAndroidTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    protected lateinit var navController: NavHostController

    @FlowPreview
    @ExperimentalCoilApi
    @Before
    fun setupNavHost() {
        composeTestRule.activity.setContent {
            navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()
            AppLayout(navController = navController, backStackEntry = backStackEntry)
        }
    }
}
