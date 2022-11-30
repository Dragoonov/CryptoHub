package com.moonlightbutterfly.cryptohub.data.assets

import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo
import kotlinx.coroutines.flow.Flow

interface CryptoAssetsDataSource {

    fun getCryptoAssetsMarketInfo(symbols: List<String>): Flow<Result<List<CryptoAssetMarketInfo>>>

    suspend fun getCryptoAssetsMarketInfo(page: Int): Result<List<CryptoAssetMarketInfo>>

    companion object {
        const val ITEMS_PER_PAGE = 50
    }
}
