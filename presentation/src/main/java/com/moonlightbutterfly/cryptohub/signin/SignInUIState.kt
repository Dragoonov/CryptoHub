package com.moonlightbutterfly.cryptohub.signin

data class SignInUIState(
    val nightModeEnabled: Boolean? = null,
    val isUserSignedIn: Boolean? = null,
    val isLoading: Boolean = true,
    val error: Throwable? = null
)
