package com.moonlightbutterfly.cryptohub.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.moonlightbutterfly.cryptohub.dataobjects.CryptoassetItem
import com.moonlightbutterfly.cryptohub.dataobjects.FavouriteCryptoasset
import com.moonlightbutterfly.cryptohub.usecases.GetCryptoassetUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetFavouritesUseCase
import com.moonlightbutterfly.cryptohub.usecases.InsertFavouriteUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveFavouriteUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class CryptoassetPanelViewModel @Inject constructor(
    private val getCryptoassetUseCase: GetCryptoassetUseCase,
    getFavouritesUseCase: GetFavouritesUseCase,
    private val insertFavouriteUseCase: InsertFavouriteUseCase,
    private val removeFavouriteUseCase: RemoveFavouriteUseCase
) : ViewModel() {

    private lateinit var cryptoasset: LiveData<CryptoassetItem>

    fun getCryptoasset(symbol: String): LiveData<CryptoassetItem> {
        if (!this::cryptoasset.isInitialized) {
            cryptoasset = liveData {
                val cryptoasset = getCryptoassetUseCase.getCryptoasset(symbol)
                emit(cryptoasset)
            }
        }
        return cryptoasset
    }

    private val favourites = getFavouritesUseCase.favourites()

    fun isCryptoInFavourites(symbol: String) = favourites.map { list ->
        list.find { it.symbol == symbol } != null
    }

    fun addCryptoToFavourites(symbol: String) {
        viewModelScope.launch {
            insertFavouriteUseCase.insertFavourite(FavouriteCryptoasset(symbol = symbol))
        }
    }

    fun removeCryptoFromFavourites(symbol: String) {
        viewModelScope.launch {
            removeFavouriteUseCase.removeFavourite(FavouriteCryptoasset(symbol = symbol))
        }
    }
}
