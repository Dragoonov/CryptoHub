package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.repository.CryptoHubExternalRepository
import com.moonlightbutterfly.cryptohub.utils.toCryptocurrencyItem
import javax.inject.Inject

class GetCryptocurrencyUseCase @Inject constructor(private val repository: CryptoHubExternalRepository) {

    suspend fun getCryptocurrency(cryptoSymbol: String) =
        repository.getCryptocurrencyOutput(cryptoSymbol).toCryptocurrencyItem()
}
