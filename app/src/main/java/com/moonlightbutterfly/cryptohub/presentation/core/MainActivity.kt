package com.moonlightbutterfly.cryptohub.presentation.core

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.di.ActivityScope
import com.moonlightbutterfly.cryptohub.di.DaggerAppComponent
import com.moonlightbutterfly.cryptohub.data.user.FirebaseAuthDataProvider
import com.moonlightbutterfly.cryptohub.presentation.ui.CryptoHubTheme
import com.moonlightbutterfly.cryptohub.presentation.ui.LocalViewModelFactory
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.AppLayout
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.ErrorHandler
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@ExperimentalCoilApi
@ActivityScope
class MainActivity : AppCompatActivity(), FirebaseAuthDataProvider {

    private lateinit var onActivityResult: (FirebaseAuthUIAuthenticationResult) -> Unit

    private lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<MainViewModel> { viewModelFactory }

    private val intentLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result ->
        onActivityResult(result)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appComponent = DaggerAppComponent.factory().create(this)
        viewModelFactory = appComponent.viewModelFactory()
        setContent {
            CompositionLocalProvider(
                LocalViewModelFactory provides viewModelFactory,
            ) {
                val isNightMode by viewModel.isNightModeEnabled.collectAsState(false)
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

    override fun getConfigurationData(): FirebaseAuthDataProvider.ConfigurationData {
        val nightModeEnabled = runBlocking { viewModel.isNightModeEnabled.first() }
        val theme = if (nightModeEnabled) {
            R.style.Theme_CryptoHub_NoActionBar_Night
        } else {
            R.style.Theme_CryptoHub_NoActionBar_Day
        }
        return FirebaseAuthDataProvider.ConfigurationData(intentLauncher, theme)
    }

    override fun setActionOnResult(action: (FirebaseAuthUIAuthenticationResult) -> Unit) {
        onActivityResult = action
    }
}
