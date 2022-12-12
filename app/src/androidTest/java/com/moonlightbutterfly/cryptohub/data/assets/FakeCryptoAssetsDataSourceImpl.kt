package com.moonlightbutterfly.cryptohub.data.assets

import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeCryptoAssetsDataSourceImpl @Inject constructor() : CryptoAssetsDataSource {

    override fun getCryptoAssetsMarketInfo(symbols: List<String>): Flow<Result<List<CryptoAssetMarketInfo>>> {
        return flowOf(Result.Success(symbols.map { CryptoAssetMarketInfo(asset = CryptoAsset(symbol = it)) }))
    }

    override fun getCryptoAssetsMarketInfo(page: Int): Flow<Result<List<CryptoAssetMarketInfo>>> {
        return flowOf(
            Result.Success(
                if (page == 1) {
                    listOf(
                        CryptoAssetMarketInfo(asset = CryptoAsset(name = "Bitcoin", symbol = "BTC")),
                        CryptoAssetMarketInfo(asset = CryptoAsset(name = "Ethereum", symbol = "ETH"))
                    )
                } else {
                    listOf()
                }
            )
        )
    }
}
