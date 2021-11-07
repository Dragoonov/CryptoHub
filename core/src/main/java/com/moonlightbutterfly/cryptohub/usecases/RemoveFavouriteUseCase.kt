package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserConfigurationRepository
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset

class RemoveFavouriteUseCase(private val userConfigurationRepository: UserConfigurationRepository) {
    suspend operator fun invoke(asset: CryptoAsset) = userConfigurationRepository.removeFavourite(asset)
}
