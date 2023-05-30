package com.moonlightbutterfly.cryptohub.panel

import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo
import com.moonlightbutterfly.cryptohub.models.NotificationConfiguration

data class CryptoAssetPanelUIState(
    val model: CryptoAssetMarketInfo? = null,
    val notificationsEnabled: Boolean? = null,
    val isInNotifications: Boolean? = null,
    val isInFavourites: Boolean? = null,
    val notificationConfiguration: NotificationConfiguration? = null,
    val isLoading: Boolean = true,
    val error: Throwable? = null
)
