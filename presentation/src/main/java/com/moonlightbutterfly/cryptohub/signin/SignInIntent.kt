package com.moonlightbutterfly.cryptohub.signin

sealed class SignInIntent {
    object GetData : SignInIntent()
    object SignIn : SignInIntent()
}
