package com.moonlightbutterfly.cryptohub.settings

sealed class SettingsIntent {
    object GetData : SettingsIntent()
    data class ChangeNightMode(val enable: Boolean) : SettingsIntent()
    object SignOut : SettingsIntent()
    object EnableNotifications : SettingsIntent()
    object DisableNotifications : SettingsIntent()
}
