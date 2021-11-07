package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserConfigurationRepository

class GetFavouritesUseCase(private val userConfigurationRepository: UserConfigurationRepository) {
    operator fun invoke() = userConfigurationRepository.getFavourites()
}
