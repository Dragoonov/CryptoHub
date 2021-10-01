package com.moonlightbutterfly.cryptohub.repository

import com.moonlightbutterfly.cryptohub.repository.dataobjects.CryptocurrencyItemOutput

interface CryptoHubExternalRepository {

    suspend fun getCryptocurrenciesOutput(page: Int): List<CryptocurrencyItemOutput>

    suspend fun getCryptocurrencyOutput(cryptoSymbol: String): CryptocurrencyItemOutput
}
