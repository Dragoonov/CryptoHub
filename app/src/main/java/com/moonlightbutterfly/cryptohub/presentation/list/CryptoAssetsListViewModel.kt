package com.moonlightbutterfly.cryptohub.presentation.list

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.moonlightbutterfly.cryptohub.data.assets.CryptoAssetsDataSource
import com.moonlightbutterfly.cryptohub.data.common.unpack
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import com.moonlightbutterfly.cryptohub.presentation.core.BaseViewModel
import com.moonlightbutterfly.cryptohub.usecases.AddFavouriteUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetAllCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetFavouritesUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoAssetsListViewModel @Inject constructor(
    getAllCryptoAssetsMarketInfoUseCase: GetAllCryptoAssetsMarketInfoUseCase,
    getCryptoAssetsMarketInfoUseCase: GetCryptoAssetsMarketInfoUseCase,
    getFavouritesUseCase: GetFavouritesUseCase,
    private val addFavouriteUseCase: AddFavouriteUseCase,
    private val removeFavouriteUseCase: RemoveFavouriteUseCase
) : BaseViewModel() {

    val cryptoAssets = Pager(PagingConfig(CryptoAssetsDataSource.ITEMS_PER_PAGE)) {
        CryptoAssetPagingSource {
            getAllCryptoAssetsMarketInfoUseCase(it).first().propagateErrors().unpack(emptyList())
        }
    }.flow.cachedIn(viewModelScope)

    private val favouriteAssets = getFavouritesUseCase()
        .prepareFlow(CryptoCollection.EMPTY)
        .map { it.unpack(CryptoCollection.EMPTY) }
        .stateIn(
            initialValue = CryptoCollection.EMPTY,
            scope = viewModelScope,
            started = WhileSubscribed(5000L)
        )

    @FlowPreview
    val favourites = favouriteAssets.flatMapConcat { collection ->
        getCryptoAssetsMarketInfoUseCase(collection.cryptoAssets.map { it.symbol })
    }
        .prepareFlow(emptyList())
        .map { it.unpack(emptyList()) }

    fun isCryptoInFavourites(asset: CryptoAsset) = favouriteAssets.map { collection ->
        collection.cryptoAssets.find { it.symbol == asset.symbol } != null
    }

    fun addToFavourites(cryptoAsset: CryptoAsset) {
        viewModelScope.launch {
            addFavouriteUseCase(cryptoAsset).propagateErrors()
        }
    }

    fun removeFromFavourites(cryptoAsset: CryptoAsset) {
        viewModelScope.launch {
            removeFavouriteUseCase(cryptoAsset).propagateErrors()
        }
    }
}
