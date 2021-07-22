package com.moonlightbutterfly.cryptohub.viewmodels

import androidx.lifecycle.ViewModel
import com.moonlightbutterfly.cryptohub.usecases.GetCryptocurrenciesListUseCase
import javax.inject.Inject

class CryptocurrenciesListViewModel @Inject constructor (getCryptocurrenciesListUseCase: GetCryptocurrenciesListUseCase) : ViewModel() {

    val cryptocurrencies = getCryptocurrenciesListUseCase.cryptocurrencies

}