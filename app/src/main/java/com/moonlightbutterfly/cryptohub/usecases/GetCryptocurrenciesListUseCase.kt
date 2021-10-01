package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.repository.CryptoHubExternalRepository
import com.moonlightbutterfly.cryptohub.utils.toCryptocurrencyItem
import javax.inject.Inject

class GetCryptocurrenciesListUseCase @Inject constructor(private val repository: CryptoHubExternalRepository) {

    suspend fun cryptocurrencies(page: Int) = repository
        .getCryptocurrenciesOutput(page)
        .map { it.toCryptocurrencyItem() }
}
