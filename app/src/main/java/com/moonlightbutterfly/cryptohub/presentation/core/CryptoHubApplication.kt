package com.moonlightbutterfly.cryptohub.presentation.core

import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CryptoHubApplication : BaseApplication() {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "general"
    }
}
