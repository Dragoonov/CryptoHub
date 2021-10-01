package com.moonlightbutterfly.cryptohub.viewmodels

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.moonlightbutterfly.cryptohub.CRYPTOCURRENCIES_LOAD_NUMBER_PER_PAGE
import com.moonlightbutterfly.cryptohub.repository.CryptocurrencyPagingSource
import com.moonlightbutterfly.cryptohub.usecases.GetCryptocurrenciesListUseCase
import javax.inject.Inject

class CryptocurrenciesListViewModel @Inject constructor (getCryptocurrenciesListUseCase: GetCryptocurrenciesListUseCase) : ViewModel() {

    val cryptocurrencies = Pager(PagingConfig(CRYPTOCURRENCIES_LOAD_NUMBER_PER_PAGE)) {
        CryptocurrencyPagingSource {
            getCryptocurrenciesListUseCase.cryptocurrencies(it)
        }
    }.flow
}
