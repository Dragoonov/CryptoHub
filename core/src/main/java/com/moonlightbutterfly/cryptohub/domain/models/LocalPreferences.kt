package com.moonlightbutterfly.cryptohub.domain.models

data class LocalPreferences(
    val nightModeEnabled: Boolean = DEFAULT_NIGHT_MODE
) {
    companion object {
        val DEFAULT = LocalPreferences()
        const val DEFAULT_NIGHT_MODE = false
    }
}
