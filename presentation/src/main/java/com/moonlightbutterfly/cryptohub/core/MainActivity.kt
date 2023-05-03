package com.moonlightbutterfly.cryptohub.core

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.findNavController
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.Constants
import com.moonlightbutterfly.cryptohub.presentation.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@ExperimentalCoilApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
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
            val isNightMode by viewModel.isNightModeEnabled.collectAsStateWithLifecycle(initialMode)
            val error by viewModel.errorMessageFlow.collectAsStateWithLifecycle(null)
            error?.let { ErrorHandler(error) }
            CryptoHubTheme(darkTheme = isNightMode) {
                AppLayout(getCurrentDestinationId, onCryptoListClicked, onSettingsClicked)
                SetStatusBarColor()
            }
        }
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
