package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserConfigurationRepository

class GetRecentsUseCase(private val userConfigurationRepository: UserConfigurationRepository) {
    operator fun invoke() = userConfigurationRepository.getRecents()
}
