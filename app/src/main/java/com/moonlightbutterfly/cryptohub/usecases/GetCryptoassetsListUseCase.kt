package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.repository.CryptoHubExternalRepository
import com.moonlightbutterfly.cryptohub.utils.toCryptoassetItem
import javax.inject.Inject

class GetCryptoassetsListUseCase @Inject constructor(private val repository: CryptoHubExternalRepository) {

    suspend fun cryptoassets(page: Int, ids: String = "") = repository
        .getCryptoassetsOutput(page = page, ids = ids)
        .map { it.toCryptoassetItem() }
}
