package com.moonlightbutterfly.cryptohub.presentation.list

import androidx.lifecycle.asLiveData
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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

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
        .propagateErrors()
        .map { it.unpack(CryptoCollection.EMPTY) }

    @FlowPreview
    val favourites = favouriteAssets.flatMapConcat { collection ->
        getCryptoAssetsMarketInfoUseCase(symbols = collection.cryptoAssets.map { it.symbol })
    }
        .propagateErrors()
        .map { it.unpack(emptyList()) }

    fun isCryptoInFavourites(asset: CryptoAsset) = favouriteAssets.map { collection ->
        collection.cryptoAssets.find { it.symbol == asset.symbol } != null
    }.asLiveData()

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
