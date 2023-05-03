package com.moonlightbutterfly.cryptohub.database.dtos

import com.moonlightbutterfly.cryptohub.models.NotificationInterval
import kotlinx.serialization.Serializable

@Serializable
internal enum class NotificationIntervalDto {
    MINUTES_30,
    HOUR,
    HOURS_2,
    HOURS_5,
    DAY,
    WEEK
}

internal fun NotificationIntervalDto.toNotificationInterval() = NotificationInterval.valueOf(this.name)
internal fun NotificationInterval.toNotificationIntervalDto() = NotificationIntervalDto.valueOf(this.name)
