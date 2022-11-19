package com.moonlightbutterfly.cryptohub.presentation.ui

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.di.DaggerTestAppComponent
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.AppLayout
import kotlinx.coroutines.FlowPreview
import org.junit.Before
import org.junit.Rule

open class CryptoHubAndroidTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    protected lateinit var navController: NavHostController

    @FlowPreview
    @ExperimentalCoilApi
    @Before
    fun setupNavHost() {
        composeTestRule.setContent {
            val appComponent = DaggerTestAppComponent.factory().create(LocalContext.current)
            val viewModelFactory = appComponent.viewModelFactory()
            CompositionLocalProvider(
                LocalViewModelFactory provides viewModelFactory,
            ) {
                navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                AppLayout(navController = navController, backStackEntry = backStackEntry)
            }
        }
    }
}
