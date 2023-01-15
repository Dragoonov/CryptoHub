package com.moonlightbutterfly.cryptohub.presentation.panel

import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.moonlightbutterfly.cryptohub.data.common.unpack
import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import com.moonlightbutterfly.cryptohub.presentation.core.BaseViewModel
import com.moonlightbutterfly.cryptohub.usecases.AddFavouriteUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetFavouritesUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoAssetPanelViewModel @Inject constructor(
    private val getCryptoAssetsMarketInfoUseCase: GetCryptoAssetsMarketInfoUseCase,
    getFavouritesUseCase: GetFavouritesUseCase,
    private val addFavouriteUseCase: AddFavouriteUseCase,
    private val removeFavouriteUseCase: RemoveFavouriteUseCase,
) : BaseViewModel() {

    private lateinit var asset: LiveData<CryptoAssetMarketInfo>

    fun getCryptoAssetMarketInfo(symbol: String): LiveData<CryptoAssetMarketInfo> {
        if (!this::asset.isInitialized) {
            asset = getCryptoAssetsMarketInfoUseCase(listOf(symbol))
                .propagateErrors()
                .map {
                    it
                        .unpack(listOf(CryptoAssetMarketInfo.EMPTY))
                        .getOrElse(0) { CryptoAssetMarketInfo.EMPTY }
                }
                .asLiveData()
        }
        return asset
    }

    private val favourites = getFavouritesUseCase()
        .propagateErrors()
        .map { it.unpack(CryptoCollection.EMPTY) }

    fun isCryptoInFavourites() = favourites.combine(asset.asFlow()) { collection, asset ->
        collection.cryptoAssets.find { it.symbol == asset.asset.symbol } != null
    }

    fun addCryptoToFavourites() {
        asset.value?.let {
            viewModelScope.launch {
                addFavouriteUseCase(it.asset).propagateErrors()
            }
        }
    }

    fun removeCryptoFromFavourites() {
        asset.value?.let {
            viewModelScope.launch {
                removeFavouriteUseCase(it.asset).propagateErrors()
            }
        }
    }
}
