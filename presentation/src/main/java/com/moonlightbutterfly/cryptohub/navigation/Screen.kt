package com.moonlightbutterfly.cryptohub.navigation

import androidx.annotation.NavigationRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ViewList
import androidx.compose.ui.graphics.vector.ImageVector
import com.moonlightbutterfly.presentation.R

enum class Screen(
    @NavigationRes val destinationId: Int,
    @StringRes val nameResourceId: Int = 0,
    val iconSelected: ImageVector = Icons.Default.Circle,
    val iconUnselected: ImageVector = Icons.Default.Circle
) {
    CRYPTO_ASSETS_LIST(
        R.id.listFragment,
        R.string.crypto_assets_list,
        Icons.Filled.ViewList,
        Icons.Outlined.ViewList
    ),
    SETTINGS(
        R.id.settingsFragment,
        R.string.settings,
        Icons.Filled.Settings,
        Icons.Outlined.Settings
    ),
    CRYPTO_ASSET_PANEL(
        R.id.panelFragment,
    ),
    SEARCH_PANEL(
        R.id.searchFragment,
    ),
    SIGN_IN_PANEL(
        R.id.signInFragment
    );
}
