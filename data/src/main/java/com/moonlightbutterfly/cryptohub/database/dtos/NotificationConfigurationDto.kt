package com.moonlightbutterfly.cryptohub.database.dtos

import com.moonlightbutterfly.cryptohub.models.NotificationConfiguration
import kotlinx.serialization.Serializable

@Serializable
internal data class NotificationConfigurationDto(
    val symbol: String,
    val notificationInterval: NotificationIntervalDto? = null,
    val notificationTime: NotificationTimeDto? = null
)

internal fun NotificationConfigurationDto.toNotificationConfiguration() = NotificationConfiguration(
    symbol, notificationInterval?.toNotificationInterval(), notificationTime?.toNotificationTime()
)
internal fun NotificationConfiguration.toNotificationConfigurationDto() = NotificationConfigurationDto(
    symbol, notificationInterval?.toNotificationIntervalDto(), notificationTime?.toNotificationTimeDto()
)
