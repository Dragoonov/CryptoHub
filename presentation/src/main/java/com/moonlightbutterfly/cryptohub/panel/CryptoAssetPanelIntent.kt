package com.moonlightbutterfly.cryptohub.panel

import com.moonlightbutterfly.cryptohub.models.NotificationConfiguration

sealed class CryptoAssetPanelIntent {
    object GetData : CryptoAssetPanelIntent()
    object AddToFavourites : CryptoAssetPanelIntent()
    object RemoveFromFavourites : CryptoAssetPanelIntent()
    data class ScheduleNotifications(val configuration: NotificationConfiguration) : CryptoAssetPanelIntent()
    object ClearNotifications : CryptoAssetPanelIntent()
}
