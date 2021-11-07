package com.moonlightbutterfly.cryptohub.data

import com.moonlightbutterfly.cryptohub.domain.models.CryptoAssetMarketInfo

/**
 * Repository class aggregating external data sources and providing coherent interface for application.
 */
class CryptoAssetsRepository(
    private val cryptoAssetsDataSource: CryptoAssetsDataSource
) {
    suspend fun getCryptoAssetsMarketInfo(
        symbols: List<String>
    ): List<CryptoAssetMarketInfo> = cryptoAssetsDataSource.getCryptoAssetsMarketInfo(symbols)

    suspend fun getCryptoAssetsMarketInfo(
        page: Int
    ): List<CryptoAssetMarketInfo> = cryptoAssetsDataSource.getCryptoAssetsMarketInfo(page)
}
