package com.moonlightbutterfly.cryptohub.data

import com.moonlightbutterfly.cryptohub.domain.models.CryptoAssetMarketInfo

interface CryptoAssetsDataSource {

    suspend fun getCryptoAssetsMarketInfo(symbols: List<String>): List<CryptoAssetMarketInfo>

    suspend fun getCryptoAssetsMarketInfo(page: Int): List<CryptoAssetMarketInfo>
}
