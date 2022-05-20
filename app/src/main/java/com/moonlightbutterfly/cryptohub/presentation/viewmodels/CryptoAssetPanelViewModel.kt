package com.moonlightbutterfly.cryptohub.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAssetMarketInfo
import com.moonlightbutterfly.cryptohub.usecases.AddFavouriteUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetFavouritesUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveFavouriteUseCase
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

class CryptoAssetPanelViewModel @Inject constructor(
    private val getCryptoAssetsMarketInfoUseCase: GetCryptoAssetsMarketInfoUseCase,
    getFavouritesUseCase: GetFavouritesUseCase,
    private val addFavouriteUseCase: AddFavouriteUseCase,
    private val removeFavouriteUseCase: RemoveFavouriteUseCase
) : ViewModel() {

    private lateinit var asset: LiveData<CryptoAssetMarketInfo>

    fun getCryptoAssetMarketInfo(symbol: String): LiveData<CryptoAssetMarketInfo> {
        if (!this::asset.isInitialized) {
            asset = liveData {
                val cryptoAsset = getCryptoAssetsMarketInfoUseCase(listOf(symbol)).first()
                emit(cryptoAsset)
            }
        }
        return asset
    }

    private val favourites = getFavouritesUseCase()

    fun isCryptoInFavourites() = favourites.combine(asset.asFlow()) { collection, asset ->
        collection.cryptoAssets.find { it.symbol == asset.asset.symbol } != null
    }

    fun addCryptoToFavourites() {
        asset.value?.let {
            viewModelScope.launch {
                addFavouriteUseCase(it.asset)
            }
        }
    }

    fun removeCryptoFromFavourites() {
        asset.value?.let {
            viewModelScope.launch {
                removeFavouriteUseCase(it.asset)
            }
        }
    }
}
