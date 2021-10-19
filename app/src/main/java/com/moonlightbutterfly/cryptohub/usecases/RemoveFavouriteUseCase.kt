package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.dataobjects.FavouriteCryptoasset
import com.moonlightbutterfly.cryptohub.repository.CryptoHubInternalRepository
import javax.inject.Inject

class RemoveFavouriteUseCase @Inject constructor(private val internalRepository: CryptoHubInternalRepository) {

    suspend fun removeFavourite(favourite: FavouriteCryptoasset) = internalRepository.removeFavourite(favourite)

}