package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.repository.CryptoHubInternalRepository
import javax.inject.Inject

class GetFavouritesUseCase @Inject constructor(private val internalRepository: CryptoHubInternalRepository) {

    fun favourites() = internalRepository.favourites

}