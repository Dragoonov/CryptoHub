package com.moonlightbutterfly.cryptohub.presentation.core

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.findNavController
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@ExperimentalCoilApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val getCurrentDestinationId = {
            try {
                findNavController(R.id.nav_host_fragment).currentDestination?.id
            } catch (exception: IllegalArgumentException) {
                null
            }
        }

        val onCryptoListClicked = {
            with(findNavController(R.id.nav_host_fragment)) {
                if (currentDestination != findDestination(R.id.listFragment)) {
                    navigate(R.id.action_listFragment)
                }
            }
        }

        val onSettingsClicked = {
            with(findNavController(R.id.nav_host_fragment)) {
                if (currentDestination != findDestination(R.id.settingsFragment)) {
                    navigate(R.id.action_settingsFragment)
                }
            }
        }
        val initialMode = runBlocking { viewModel.isNightModeEnabled.first() }
        setContent {
            val isNightMode by viewModel.isNightModeEnabled.collectAsState(initialMode)
            val error by viewModel.errorMessageFlow.collectAsState(null)
            error?.let { ErrorHandler(error) }
            CryptoHubTheme(darkTheme = isNightMode) {
                AppLayout(getCurrentDestinationId, onCryptoListClicked, onSettingsClicked)
                SetStatusBarColor()
            }
        }
    }
}
