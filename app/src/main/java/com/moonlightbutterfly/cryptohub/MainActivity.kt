package com.moonlightbutterfly.cryptohub

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
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.moonlightbutterfly.cryptohub.di.DaggerAppComponent
import com.moonlightbutterfly.cryptohub.ui.CryptoHubTheme
import com.moonlightbutterfly.cryptohub.ui.LocalViewModelFactory
import com.moonlightbutterfly.cryptohub.ui.composables.AppLayout
import com.moonlightbutterfly.cryptohub.viewmodels.MainViewModel
import com.moonlightbutterfly.cryptohub.viewmodels.ViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var viewModelFactory: ViewModelFactory

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
