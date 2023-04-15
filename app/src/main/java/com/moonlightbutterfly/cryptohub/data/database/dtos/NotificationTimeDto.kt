package com.moonlightbutterfly.cryptohub.data.database.dtos

import com.moonlightbutterfly.cryptohub.models.NotificationTime
import kotlinx.serialization.Serializable

@Serializable
data class NotificationTimeDto(val hour: Int, val minute: Int)

fun NotificationTimeDto.toNotificationTime() = NotificationTime(hour, minute)
fun NotificationTime.toNotificationTimeDto() = NotificationTimeDto(hour, minute)
