package com.moonlightbutterfly.cryptohub.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ViewList
import androidx.compose.ui.graphics.vector.ImageVector
import com.moonlightbutterfly.cryptohub.R

enum class Screen(val route: String,
                  @StringRes val nameResourceId: Int,
                  val iconSelected: ImageVector,
                  val iconUnselected: ImageVector) {
    CRYPTOCURRENCIES_LIST(
        "cryptocurrenciesList",
        R.string.cryptocurrencies_list,
        Icons.Filled.ViewList,
        Icons.Outlined.ViewList),
    SETTINGS("settings",
        R.string.settings,
        Icons.Filled.Settings,
        Icons.Outlined.Settings)
}