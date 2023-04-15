package com.moonlightbutterfly.cryptohub.data.database.dtos

import com.moonlightbutterfly.cryptohub.models.NotificationConfiguration
import kotlinx.serialization.Serializable

@Serializable
data class NotificationConfigurationDto(
    val symbol: String,
    val notificationInterval: NotificationIntervalDto? = null,
    val notificationTime: NotificationTimeDto? = null
)

fun NotificationConfigurationDto.toNotificationConfiguration() = NotificationConfiguration(
    symbol, notificationInterval?.toNotificationInterval(), notificationTime?.toNotificationTime()
)
fun NotificationConfiguration.toNotificationConfigurationDto() = NotificationConfigurationDto(
    symbol, notificationInterval?.toNotificationIntervalDto(), notificationTime?.toNotificationTimeDto()
)
