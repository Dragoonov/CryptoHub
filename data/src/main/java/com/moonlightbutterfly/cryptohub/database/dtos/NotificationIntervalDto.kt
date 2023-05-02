package com.moonlightbutterfly.cryptohub.database.dtos

import com.moonlightbutterfly.cryptohub.models.NotificationInterval
import kotlinx.serialization.Serializable

@Serializable
enum class NotificationIntervalDto {
    MINUTES_30,
    HOUR,
    HOURS_2,
    HOURS_5,
    DAY,
    WEEK
}

fun NotificationIntervalDto.toNotificationInterval() = NotificationInterval.valueOf(this.name)
fun NotificationInterval.toNotificationIntervalDto() = NotificationIntervalDto.valueOf(this.name)