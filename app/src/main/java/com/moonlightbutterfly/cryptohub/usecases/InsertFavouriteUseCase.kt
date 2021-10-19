package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.dataobjects.FavouriteCryptoasset
import com.moonlightbutterfly.cryptohub.repository.CryptoHubInternalRepository
import javax.inject.Inject

class InsertFavouriteUseCase @Inject constructor(private val internalRepository: CryptoHubInternalRepository) {

    suspend fun insertFavourite(favourite: FavouriteCryptoasset) = internalRepository.insertFavourite(favourite)

}