package com.moonlightbutterfly.cryptohub.repository

import androidx.paging.PagingSource
import com.moonlightbutterfly.cryptohub.repository.dataobjects.CryptocurrencyOutput
import javax.inject.Inject

class CryptoHubExternalRepositoryFakeImpl @Inject constructor() : CryptoHubExternalRepository {

    override fun getCryptocurrencyOutputsSource(): PagingSource<Int, CryptocurrencyOutput> = CryptocurrencyPagingSource {
        if (it == 1) {
            listOf(
                CryptocurrencyOutput(
                    name = "Bitcoin"
                ),
                CryptocurrencyOutput(
                    name = "Ethereum"
                )
            )
        } else listOf(CryptocurrencyOutput())
    }
}
