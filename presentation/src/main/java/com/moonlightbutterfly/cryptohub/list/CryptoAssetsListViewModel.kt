package com.moonlightbutterfly.cryptohub.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.moonlightbutterfly.cryptohub.data.assets.CryptoAssetsDataSource
import com.moonlightbutterfly.cryptohub.data.common.unpack
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import com.moonlightbutterfly.cryptohub.usecases.AddFavouriteUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetAllCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetFavouritesUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoAssetsListViewModel @Inject constructor(
    getAllCryptoAssetsMarketInfoUseCase: GetAllCryptoAssetsMarketInfoUseCase,
    getCryptoAssetsMarketInfoUseCase: GetCryptoAssetsMarketInfoUseCase,
    getFavouritesUseCase: GetFavouritesUseCase,
    private val addFavouriteUseCase: AddFavouriteUseCase,
    private val removeFavouriteUseCase: RemoveFavouriteUseCase
) : ViewModel() {

    val cryptoAssets = Pager(PagingConfig(CryptoAssetsDataSource.ITEMS_PER_PAGE)) {
        CryptoAssetPagingSource {
            getAllCryptoAssetsMarketInfoUseCase(it).first().unpack(emptyList())
        }
    }.flow.cachedIn(viewModelScope)

    private val favouriteAssets = getFavouritesUseCase()
        .map { it.unpack(CryptoCollection.EMPTY) }

    @FlowPreview
    val favourites = favouriteAssets.flatMapConcat { collection ->
        getCryptoAssetsMarketInfoUseCase(collection.cryptoAssets.map { it.symbol })
    }
        .map { it.unpack(emptyList()) }

    fun isCryptoInFavourites(asset: CryptoAsset) = favouriteAssets.map { collection ->
        collection.cryptoAssets.find { it.symbol == asset.symbol } != null
    }

    fun addToFavourites(cryptoAsset: CryptoAsset) {
        viewModelScope.launch {
            addFavouriteUseCase(cryptoAsset)
        }
    }

    fun removeFromFavourites(cryptoAsset: CryptoAsset) {
        viewModelScope.launch {
            removeFavouriteUseCase(cryptoAsset)
        }
    }
}
