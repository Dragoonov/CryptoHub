package com.moonlightbutterfly.cryptohub.data.assets

import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo
import kotlinx.coroutines.flow.Flow

interface CryptoAssetsDataSource {

    fun getCryptoAssetsMarketInfo(symbols: List<String>): Flow<Answer<List<CryptoAssetMarketInfo>>>

    fun getCryptoAssetsMarketInfo(page: Int): Flow<Answer<List<CryptoAssetMarketInfo>>>

    companion object {
        const val ITEMS_PER_PAGE = 50
    }
}
