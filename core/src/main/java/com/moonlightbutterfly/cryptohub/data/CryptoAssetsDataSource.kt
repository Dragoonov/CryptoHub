package com.moonlightbutterfly.cryptohub.data

import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo
import kotlinx.coroutines.flow.Flow

interface CryptoAssetsDataSource {

    fun getCryptoAssetsMarketInfo(symbols: List<String>): Flow<Result<List<CryptoAssetMarketInfo>>>

    suspend fun getCryptoAssetsMarketInfo(page: Int): Result<List<CryptoAssetMarketInfo>>
}
