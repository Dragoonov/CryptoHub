package com.moonlightbutterfly.cryptohub.presentation.ui

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
    CRYPTO_ASSETS_LIST(
        Routes.CRYPTO_ASSETS_LIST,
        R.string.crypto_assets_list,
        Icons.Filled.ViewList,
        Icons.Outlined.ViewList
    ),
    SETTINGS(
        Routes.SETTINGS,
        R.string.settings,
        Icons.Filled.Settings,
        Icons.Outlined.Settings
    ),
    CRYPTO_ASSET_PANEL(
        Routes.CRYPTO_PANEL,
    ),
    SEARCH_PANEL(
        Routes.SEARCH_PANEL,
    ),
    SIGN_IN_PANEL(
        Routes.SIGN_IN_PANEL
    ),
    EMAIL_SIGN_IN_PANEL(
        Routes.EMAIL_SIGN_IN_PANEL
    ),
    PHONE_SIGN_IN_PANEL(
        Routes.PHONE_SIGN_IN_PANEL
    );

    companion object {
        fun bottomNavigationScreens(): List<Screen> = listOf(CRYPTO_ASSETS_LIST, SETTINGS)
        fun String.isSignInScreen(): Boolean = listOf(
            SIGN_IN_PANEL,
            EMAIL_SIGN_IN_PANEL,
            PHONE_SIGN_IN_PANEL
        ).map { it.route }.contains(this)
    }
}
