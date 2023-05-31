package com.moonlightbutterfly.cryptohub.settings

data class SettingsUIState(
    val nightModeEnabled: Boolean? = null,
    val notificationsEnabled: Boolean? = null,
    val notificationSymbols: List<String> = emptyList(),
    val isUserSignedIn: Boolean? = null,
    val isLoading: Boolean = true,
    val error: Throwable? = null
)
