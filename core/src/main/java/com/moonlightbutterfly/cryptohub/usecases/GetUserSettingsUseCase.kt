package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserConfigurationRepository

class GetUserSettingsUseCase(private val userConfigurationRepository: UserConfigurationRepository) {
    operator fun invoke() = userConfigurationRepository.getUserSettings()
}
