package com.moonlightbutterfly.cryptohub.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.moonlightbutterfly.cryptohub.CRYPTOASSETS_LOAD_NUMBER_PER_PAGE
import com.moonlightbutterfly.cryptohub.dataobjects.FavouriteCryptoasset
import com.moonlightbutterfly.cryptohub.repository.CryptoassetPagingSource
import com.moonlightbutterfly.cryptohub.usecases.GetCryptoassetsListUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetFavouritesUseCase
import com.moonlightbutterfly.cryptohub.usecases.InsertFavouriteUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveFavouriteUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.launch
import javax.inject.Inject

class CryptoassetsListViewModel @Inject constructor(
    getCryptoassetsListUseCase: GetCryptoassetsListUseCase,
    getFavouritesUseCase: GetFavouritesUseCase,
    private val insertFavouriteUseCase: InsertFavouriteUseCase,
    private val removeFavouriteUseCase: RemoveFavouriteUseCase
) : ViewModel() {

    val cryptoassets = Pager(PagingConfig(CRYPTOASSETS_LOAD_NUMBER_PER_PAGE)) {
        CryptoassetPagingSource {
            getCryptoassetsListUseCase.cryptoassets(it)
        }
    }.flow

    private val favouritesSymbols = getFavouritesUseCase.favourites()

    @ExperimentalCoroutinesApi
    @FlowPreview
    val favourites by lazy {
        favouritesSymbols.asFlow().flatMapConcat { list ->
            val mapped = list.joinToString(separator = ",") { it.symbol }
            Pager(PagingConfig(CRYPTOASSETS_LOAD_NUMBER_PER_PAGE)) {
                CryptoassetPagingSource { page ->
                    getCryptoassetsListUseCase.cryptoassets(page = page, ids = mapped)
                }
            }.flow
        }
    }

    fun isCryptoInFavourites(symbol: String) = favouritesSymbols.map { list ->
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
