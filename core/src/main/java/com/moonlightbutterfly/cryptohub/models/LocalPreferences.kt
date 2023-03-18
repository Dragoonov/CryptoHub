package com.moonlightbutterfly.cryptohub.models

data class LocalPreferences(
    val nightModeEnabled: Boolean = DEFAULT_NIGHT_MODE,
    val notificationsConfiguration: Set<NotificationConfiguration> = emptySet(),
    val notificationsEnabled: Boolean = DEFAULT_NOTIFICATIONS_ENABLED
) {
    companion object {
        val DEFAULT = LocalPreferences()
        const val DEFAULT_NIGHT_MODE = false
        const val DEFAULT_NOTIFICATIONS_ENABLED = true
    }
}
