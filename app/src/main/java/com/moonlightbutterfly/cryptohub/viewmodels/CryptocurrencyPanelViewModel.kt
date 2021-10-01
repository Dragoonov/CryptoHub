package com.moonlightbutterfly.cryptohub.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.moonlightbutterfly.cryptohub.dataobjects.CryptocurrencyItem
import com.moonlightbutterfly.cryptohub.usecases.GetCryptocurrencyUseCase
import javax.inject.Inject

class CryptocurrencyPanelViewModel @Inject constructor(
    private val getCryptocurrencyUseCase: GetCryptocurrencyUseCase
) : ViewModel() {

    private lateinit var cryptocurrency: LiveData<CryptocurrencyItem>

    fun getCryptocurrency(symbol: String): LiveData<CryptocurrencyItem> {
        if (!this::cryptocurrency.isInitialized) {
            cryptocurrency = liveData {
                val cryptocurrency = getCryptocurrencyUseCase.getCryptocurrency(symbol)
                emit(cryptocurrency)
            }
        }
        return cryptocurrency
    }
}
