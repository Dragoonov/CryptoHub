package com.moonlightbutterfly.cryptohub.repository

import com.moonlightbutterfly.cryptohub.repository.dataobjects.CryptocurrencyItemOutput
import javax.inject.Inject

class CryptoHubExternalRepositoryFakeImpl @Inject constructor() : CryptoHubExternalRepository {

    override suspend fun getCryptocurrenciesOutput(page: Int): List<CryptocurrencyItemOutput> =
        if (page == 1) {
            listOf(
                CryptocurrencyItemOutput(
                    name = "Bitcoin",
                    symbol = "BTC"
                ),
                CryptocurrencyItemOutput(
                    name = "Ethereum",
                    symbol = "ETH"
                )
            )
        } else listOf(CryptocurrencyItemOutput())

    override suspend fun getCryptocurrencyOutput(cryptoSymbol: String): CryptocurrencyItemOutput =
        if (cryptoSymbol == "BTC") {
            CryptocurrencyItemOutput(
                name = "Bitcoin",
                symbol = "BTC"
            )
        } else {
            CryptocurrencyItemOutput(
                name = "Ethereum",
                symbol = "ETH"
            )
        }
}
