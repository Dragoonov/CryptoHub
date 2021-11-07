package com.moonlightbutterfly.cryptohub.repository

import com.moonlightbutterfly.cryptohub.data.CryptoAssetsDataSource
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAssetMarketInfo
import javax.inject.Inject

class FakeCryptoAssetsDataSourceImpl @Inject constructor() : CryptoAssetsDataSource {

    override suspend fun getCryptoAssetsMarketInfo(symbols: List<String>): List<CryptoAssetMarketInfo> {
        return symbols.map { CryptoAssetMarketInfo(asset = CryptoAsset(symbol = it)) }
    }

    override suspend fun getCryptoAssetsMarketInfo(page: Int): List<CryptoAssetMarketInfo> {
        return if (page == 1) {
            listOf(
                CryptoAssetMarketInfo(asset = CryptoAsset(name = "Bitcoin", symbol = "BTC")),
                CryptoAssetMarketInfo(asset = CryptoAsset(name = "Ethereum", symbol = "ETH"))
            )
        } else listOf()
    }
}
