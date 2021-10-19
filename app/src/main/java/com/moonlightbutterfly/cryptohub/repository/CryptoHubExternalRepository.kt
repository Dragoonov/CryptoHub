package com.moonlightbutterfly.cryptohub.repository

import com.moonlightbutterfly.cryptohub.repository.dataobjects.CryptoassetItemOutput

interface CryptoHubExternalRepository {

    suspend fun getCryptoassetsOutput(ids: String = "", page: Int): List<CryptoassetItemOutput>

    suspend fun getCryptoassetOutput(cryptoSymbol: String): CryptoassetItemOutput
}
