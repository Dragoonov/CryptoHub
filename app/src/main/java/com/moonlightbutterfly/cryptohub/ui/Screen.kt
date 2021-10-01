package com.moonlightbutterfly.cryptohub.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ViewList
import androidx.compose.ui.graphics.vector.ImageVector
import com.moonlightbutterfly.cryptohub.R

enum class Screen(
    val route: String,
    @StringRes val nameResourceId: Int = 0,
    val iconSelected: ImageVector = Icons.Default.Circle,
    val iconUnselected: ImageVector = Icons.Default.Circle
) {
    CRYPTOCURRENCIES_LIST(
        "cryptocurrenciesList",
        R.string.cryptocurrencies_list,
        Icons.Filled.ViewList,
        Icons.Outlined.ViewList
    ),
    SETTINGS(
        "settings",
        R.string.settings,
        Icons.Filled.Settings,
        Icons.Outlined.Settings
    ),
    CRYPTOCURRENCY_PANEL(
        "cryptocurrencyPanel",
    );

    companion object {
        fun bottomNavigationScreens(): List<Screen> = listOf(CRYPTOCURRENCIES_LIST, SETTINGS)
    }
}
