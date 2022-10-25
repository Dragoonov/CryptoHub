package com.moonlightbutterfly.cryptohub.repository

import com.moonlightbutterfly.cryptohub.data.CryptoAssetsDataSource
import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeCryptoAssetsDataSourceImpl @Inject constructor() : CryptoAssetsDataSource {

    override fun getCryptoAssetsMarketInfo(symbols: List<String>): Flow<Result<List<CryptoAssetMarketInfo>>> {
        return flowOf(Result.Success(symbols.map { CryptoAssetMarketInfo(asset = CryptoAsset(symbol = it)) }))
    }

    override suspend fun getCryptoAssetsMarketInfo(page: Int): Result<List<CryptoAssetMarketInfo>> {
        return Result.Success(
            if (page == 1) {
                listOf(
                    CryptoAssetMarketInfo(asset = CryptoAsset(name = "Bitcoin", symbol = "BTC")),
                    CryptoAssetMarketInfo(asset = CryptoAsset(name = "Ethereum", symbol = "ETH"))
                )
            } else {
                listOf()
            }
        )
    }
}
