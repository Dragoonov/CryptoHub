package com.moonlightbutterfly.cryptohub.presentation.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.moonlightbutterfly.cryptohub.di.DaggerAppComponent
import com.moonlightbutterfly.cryptohub.presentation.ViewModelFactory
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.AppLayout
import com.moonlightbutterfly.cryptohub.presentation.viewmodels.MainViewModel

@ExperimentalCoilApi
class MainActivity : AppCompatActivity() {

    private lateinit var viewModelFactory: ViewModelFactory
    private val signInFlowProvider: SignInFlowProvider = SignInFlowProviderImpl(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModelFactory = DaggerAppComponent.factory().create(this).viewModelFactory()
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(LocalViewModelFactory provides viewModelFactory) {
                val viewModel: MainViewModel = viewModel(factory = LocalViewModelFactory.current)
                val isNightMode by viewModel.isNightModeEnabled.observeAsState(false)
                CryptoHubTheme(darkTheme = isNightMode) {
                    val navController = rememberNavController()
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    AppLayout(navController, backStackEntry, signInFlowProvider)
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
