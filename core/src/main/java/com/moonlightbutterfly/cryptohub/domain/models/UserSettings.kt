package com.moonlightbutterfly.cryptohub.domain.models

data class UserSettings(
    val nightModeEnabled: Boolean = EMPTY_NIGHT_MODE
) {
    companion object {
        val EMPTY = UserSettings()
        const val EMPTY_NIGHT_MODE = false
    }
}
