package com.moonlightbutterfly.cryptohub.repository

import androidx.paging.PagingSource
import com.moonlightbutterfly.cryptohub.repository.dataobjects.CryptocurrencyOutput

interface CryptoHubExternalRepository {

    fun getCryptocurrencyOutputsSource(): PagingSource<Int, CryptocurrencyOutput>
}
