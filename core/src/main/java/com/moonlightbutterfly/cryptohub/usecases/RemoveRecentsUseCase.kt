package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserConfigurationRepository

class RemoveRecentsUseCase(private val userConfigurationRepository: UserConfigurationRepository) {
    suspend operator fun invoke() = userConfigurationRepository.removeRecents()
}
