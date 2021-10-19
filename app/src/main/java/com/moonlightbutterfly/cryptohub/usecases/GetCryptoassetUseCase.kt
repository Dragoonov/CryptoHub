package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.repository.CryptoHubExternalRepository
import com.moonlightbutterfly.cryptohub.utils.toCryptoassetItem
import javax.inject.Inject

class GetCryptoassetUseCase @Inject constructor(private val repository: CryptoHubExternalRepository) {

    suspend fun getCryptoasset(cryptoSymbol: String) =
        repository.getCryptoassetOutput(cryptoSymbol).toCryptoassetItem()
}
