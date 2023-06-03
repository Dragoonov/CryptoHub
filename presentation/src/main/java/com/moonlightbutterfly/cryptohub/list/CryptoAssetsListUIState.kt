package com.moonlightbutterfly.cryptohub.list

import androidx.paging.PagingData
import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class CryptoAssetsListUIState(
    val assets: Flow<PagingData<CryptoAssetMarketInfo>> = emptyFlow(),
    val favourites: List<CryptoAssetMarketInfo> = emptyList(),
    val isLoading: Boolean = true,
    val error: Throwable? = null
)
