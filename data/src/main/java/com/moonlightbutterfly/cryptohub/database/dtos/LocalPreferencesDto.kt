package com.moonlightbutterfly.cryptohub.database.dtos

import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import kotlinx.serialization.Serializable

@Serializable
internal data class LocalPreferencesDto(
    val nightModeEnabled: Boolean,
    val notificationsConfiguration: Set<NotificationConfigurationDto>,
    val notificationsEnabled: Boolean
)

internal fun LocalPreferencesDto.toLocalPreferences(): LocalPreferences = LocalPreferences(
    nightModeEnabled,
    notificationsConfiguration.map { it.toNotificationConfiguration() }.toSet(),
    notificationsEnabled
)

internal fun LocalPreferences.toLocalPreferencesDto(): LocalPreferencesDto = LocalPreferencesDto(
    nightModeEnabled,
    notificationsConfiguration.map { it.toNotificationConfigurationDto() }.toSet(),
    notificationsEnabled
)
