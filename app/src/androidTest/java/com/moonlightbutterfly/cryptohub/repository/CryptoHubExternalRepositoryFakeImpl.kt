package com.moonlightbutterfly.cryptohub.repository

import com.moonlightbutterfly.cryptohub.repository.dataobjects.CryptoassetItemOutput
import javax.inject.Inject

class CryptoHubExternalRepositoryFakeImpl @Inject constructor() : CryptoHubExternalRepository {

    override suspend fun getCryptoassetsOutput(page: Int): List<CryptoassetItemOutput> =
        if (page == 1) {
            listOf(
                CryptoassetItemOutput(
                    name = "Bitcoin",
                    symbol = "BTC"
                ),
                CryptoassetItemOutput(
                    name = "Ethereum",
                    symbol = "ETH"
                )
            )
        } else listOf(CryptoassetItemOutput())

    override suspend fun getCryptoassetOutput(cryptoSymbol: String): CryptoassetItemOutput =
        if (cryptoSymbol == "BTC") {
            CryptoassetItemOutput(
                name = "Bitcoin",
                symbol = "BTC"
            )
        } else {
            CryptoassetItemOutput(
                name = "Ethereum",
                symbol = "ETH"
            )
        }
}
