package com.moonlightbutterfly.cryptohub.core

sealed class MainIntent {
    object GetData : MainIntent()
}
