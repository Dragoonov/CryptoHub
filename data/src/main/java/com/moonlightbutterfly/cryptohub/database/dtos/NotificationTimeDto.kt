package com.moonlightbutterfly.cryptohub.database.dtos

import com.moonlightbutterfly.cryptohub.models.NotificationTime
import kotlinx.serialization.Serializable

@Serializable
internal data class NotificationTimeDto(val hour: Int, val minute: Int)

internal fun NotificationTimeDto.toNotificationTime() = NotificationTime(hour, minute)
internal fun NotificationTime.toNotificationTimeDto() = NotificationTimeDto(hour, minute)
