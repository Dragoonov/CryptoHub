package com.moonlightbutterfly.cryptohub.core

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidViewBinding
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.navigation.Screen
import com.moonlightbutterfly.presentation.databinding.ActivityMainBinding
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoilApi
@Composable
fun AppLayout(
    getCurrentDestinationId: () -> Int?,
    onCryptoListSelected: () -> Unit,
    onSettingsSelected: () -> Unit,
) {
    Scaffold(
        bottomBar = {
            AppBottomNavigation(getCurrentDestinationId, onCryptoListSelected, onSettingsSelected)
        }
    ) {
        AndroidViewBinding(
            ActivityMainBinding::inflate,
            Modifier.padding(bottom = it.calculateBottomPadding())
        )
    }
}

@Composable
fun AppBottomNavigation(
    getCurrentDestinationId: () -> Int?,
    onCryptoListSelected: () -> Unit,
    onSettingsSelected: () -> Unit,
) {
    BottomNavigation {
        var currentDestinationId: Int? by remember { mutableStateOf(Int.MAX_VALUE) }
        val isInitialDestination = currentDestinationId == Int.MAX_VALUE
        val isListDestination = currentDestinationId == Screen.CRYPTO_ASSETS_LIST.destinationId
        val listSelected = isInitialDestination || isListDestination
        val settingsSelected = currentDestinationId == Screen.SETTINGS.destinationId
        BottomNavigationItem(
            icon = {
                Icon(
                    if (listSelected) {
                        Screen.CRYPTO_ASSETS_LIST.iconSelected
                    } else {
                        Screen.CRYPTO_ASSETS_LIST.iconUnselected
                    },
                    contentDescription = null
                )
            },
            label = { Text(stringResource(Screen.CRYPTO_ASSETS_LIST.nameResourceId)) },
            selected = listSelected,
            onClick = {
                onCryptoListSelected()
                currentDestinationId = getCurrentDestinationId()
            }
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    if (settingsSelected) Screen.SETTINGS.iconSelected else Screen.SETTINGS.iconUnselected,
                    contentDescription = null
                )
            },
            label = { Text(stringResource(Screen.SETTINGS.nameResourceId)) },
            selected = settingsSelected,
            onClick = {
                onSettingsSelected()
                currentDestinationId = getCurrentDestinationId()
            }
        )
    }
}
