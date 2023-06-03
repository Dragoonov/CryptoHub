package com.moonlightbutterfly.cryptohub.list

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.moonlightbutterfly.cryptohub.core.BaseViewModel
import com.moonlightbutterfly.cryptohub.data.assets.CryptoAssetsDataSource
import com.moonlightbutterfly.cryptohub.data.common.getOrNull
import com.moonlightbutterfly.cryptohub.data.common.unpack
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo
import com.moonlightbutterfly.cryptohub.usecases.AddFavouriteUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetAllCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetFavouritesUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class CryptoAssetsListViewModel @Inject constructor(
    private val getAllCryptoAssetsMarketInfoUseCase: GetAllCryptoAssetsMarketInfoUseCase,
    private val getCryptoAssetsMarketInfoUseCase: GetCryptoAssetsMarketInfoUseCase,
    private val getFavouritesUseCase: GetFavouritesUseCase,
    private val addFavouriteUseCase: AddFavouriteUseCase,
    private val removeFavouriteUseCase: RemoveFavouriteUseCase,
    initialState: CryptoAssetsListUIState
) : BaseViewModel<CryptoAssetsListIntent, CryptoAssetsListUIState>(initialState) {

    init {
        acceptIntent(CryptoAssetsListIntent.GetData)
    }

    private fun produceAssetsFlow() = Pager(PagingConfig(CryptoAssetsDataSource.ITEMS_PER_PAGE)) {
        CryptoAssetPagingSource {
            getAllCryptoAssetsMarketInfoUseCase(it).first().unpack(emptyList())
        }
    }.flow.cachedIn(viewModelScope)

    private suspend fun produceFavourites() =
        with(getFavouritesUseCase().first().getOrNull()?.cryptoAssets ?: emptyList()) {
            getCryptoAssetsMarketInfoUseCase(map { it.symbol })
        }.first().unpack(emptyList())

    private fun getData(): Flow<CryptoAssetsListUIState> = flow {
        try {
            val assets = produceAssetsFlow()
            val favourites = produceFavourites()
            emit(
                CryptoAssetsListUIState(
                    assets = assets,
                    favourites = favourites,
                    isLoading = false,
                    error = null
                )
            )
        } catch (exception: Exception) {
            emit(CryptoAssetsListUIState(isLoading = false, error = exception))
        }
    }

    fun isCryptoInFavourites(asset: CryptoAsset) = uiState.value.favourites.find {
        it.asset.symbol == asset.symbol
    } != null

    private fun addCryptoToFavourites(asset: CryptoAssetMarketInfo) = flow {
        addFavouriteUseCase(asset.asset).getOrNull()?.let {
            this.refreshFavourites()
        }
    }

    private fun removeCryptoFromFavourites(asset: CryptoAssetMarketInfo) = flow {
        removeFavouriteUseCase(asset.asset).getOrNull()?.let {
            this.refreshFavourites()
        }
    }

    private suspend fun FlowCollector<CryptoAssetsListUIState>.refreshFavourites() {
        emit(uiState.value.copy(isLoading = true))
        emit(
            uiState.value.copy(
                favourites = produceFavourites(),
                isLoading = false
            )
        )
    }

    override fun mapIntent(intent: CryptoAssetsListIntent): Flow<CryptoAssetsListUIState> {
        return when (intent) {
            CryptoAssetsListIntent.GetData -> getData()
            is CryptoAssetsListIntent.AddToFavourites -> addCryptoToFavourites(intent.asset)
            is CryptoAssetsListIntent.RemoveFromFavourites -> removeCryptoFromFavourites(intent.asset)
        }
    }
}
