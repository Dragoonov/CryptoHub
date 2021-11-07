package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserConfigurationRepository
import com.moonlightbutterfly.cryptohub.domain.models.UserSettings

class UpdateUserSettingsUseCase(private val userConfigurationRepository: UserConfigurationRepository) {
    suspend operator fun invoke(userSettings: UserSettings) = userConfigurationRepository.updateUserSettings(userSettings)
}
