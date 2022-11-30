package com.moonlightbutterfly.cryptohub.data.assets

/**
 * Repository class aggregating external data sources and providing coherent interface for application.
 */
class CryptoAssetsRepository(
    private val cryptoAssetsDataSource: CryptoAssetsDataSource
) {
    fun getCryptoAssetsMarketInfo(symbols: List<String>) = cryptoAssetsDataSource.getCryptoAssetsMarketInfo(symbols)

    suspend fun getCryptoAssetsMarketInfo(page: Int) = cryptoAssetsDataSource.getCryptoAssetsMarketInfo(page)
}
