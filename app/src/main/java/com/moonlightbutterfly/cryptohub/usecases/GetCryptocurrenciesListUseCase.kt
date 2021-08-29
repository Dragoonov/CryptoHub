package com.moonlightbutterfly.cryptohub.usecases

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.moonlightbutterfly.cryptohub.CRYPTOCURRENCIES_LOAD_NUMBER_PER_PAGE
import com.moonlightbutterfly.cryptohub.dataobjects.CryptocurrencyListItem
import com.moonlightbutterfly.cryptohub.repository.CryptoHubExternalRepository
import com.moonlightbutterfly.cryptohub.repository.retrofit.dataobjects.CryptocurrencyOutput
import com.moonlightbutterfly.cryptohub.utils.round
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCryptocurrenciesListUseCase @Inject constructor(repository: CryptoHubExternalRepository) {

    val cryptocurrencies = Pager(PagingConfig(CRYPTOCURRENCIES_LOAD_NUMBER_PER_PAGE)) {
        repository.getCryptocurrencyOutputsSource()
    }.flow.map {
        it.mapToCryptocurrencyBusinessModel()
    }

    private fun PagingData<CryptocurrencyOutput>.mapToCryptocurrencyBusinessModel() = map {
        CryptocurrencyListItem(
            name = it.name ?: "",
            symbol = it.symbol ?: "",
            logoUrl = it.logoUrl ?: "",
            price = it.price?.toDouble()?.round(2) ?: .00,
            priceChange = it.dayChangesOutput?.priceChangePercent?.toDouble()?.round(2) ?: .00,
            marketCap = it.marketCap?.toDouble()?.round(2) ?: .00
        )
    }
}
