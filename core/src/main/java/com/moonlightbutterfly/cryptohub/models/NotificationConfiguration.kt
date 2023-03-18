package com.moonlightbutterfly.cryptohub.models

data class NotificationConfiguration(
    val symbol: String,
    val notificationInterval: NotificationInterval? = null,
    val notificationTime: NotificationTime? = null
)
