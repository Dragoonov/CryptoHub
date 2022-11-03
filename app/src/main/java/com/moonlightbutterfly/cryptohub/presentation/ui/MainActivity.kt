package com.moonlightbutterfly.cryptohub.presentation.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.moonlightbutterfly.cryptohub.di.ActivityScope
import com.moonlightbutterfly.cryptohub.di.DaggerAppComponent
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.AppLayout
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.ErrorHandler
import com.moonlightbutterfly.cryptohub.presentation.viewmodels.MainViewModel

@ExperimentalCoilApi
@ActivityScope
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appComponent = DaggerAppComponent.factory().create(this)
        val signInController = appComponent.googleSignInIntentController()
        val viewModelFactory = appComponent.viewModelFactory()
        setContent {
            CompositionLocalProvider(
                LocalViewModelFactory provides viewModelFactory,
                LocalSignInIntentControllerProvider provides signInController
            ) {
                val viewModel: MainViewModel = viewModel(factory = LocalViewModelFactory.current)
                val isNightMode by viewModel.isNightModeEnabled.observeAsState(false)
                val error by viewModel.errorMessageFlow.collectAsState(null)
                error?.let { ErrorHandler(error) }
                CryptoHubTheme(darkTheme = isNightMode) {
                    val navController = rememberNavController()
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    AppLayout(navController, backStackEntry)
                    SetStatusBarColor()
                }
            }
        }
    }

    @Composable
    private fun SetStatusBarColor() {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = MaterialTheme.colors.isLight
        val statusBarColor = MaterialTheme.colors.primary
        SideEffect {
            systemUiController.setStatusBarColor(
                color = statusBarColor,
                darkIcons = useDarkIcons
            )
        }
    }
}
