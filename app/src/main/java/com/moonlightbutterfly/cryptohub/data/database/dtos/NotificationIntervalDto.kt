package com.moonlightbutterfly.cryptohub.data.database.dtos

import com.moonlightbutterfly.cryptohub.models.NotificationInterval

@kotlinx.serialization.Serializable
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
