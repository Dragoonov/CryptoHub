package com.moonlightbutterfly.cryptohub.repository

import androidx.paging.PagingSource
import com.google.gson.GsonBuilder
import com.moonlightbutterfly.cryptohub.repository.retrofit.NomicsService
import com.moonlightbutterfly.cryptohub.repository.retrofit.dataobjects.CryptocurrencyOutput
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

/**
 * Repository returning the info about cryptocurrencies, using the Nomics (https://nomics.com/docs) API.
 */
class CryptoHubExternalRepositoryImpl @Inject constructor() : CryptoHubExternalRepository {
    private val service by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.nomics.com/v1/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(NomicsService::class.java)
    }

    override fun getCryptocurrencyOutputsSource(): PagingSource<Int, CryptocurrencyOutput> = CryptocurrencyPagingSource {
        getCryptocurrencyOutputs(it)
    }

    private suspend fun getCryptocurrencyOutputs(page: Int): List<CryptocurrencyOutput> {
        try {
            return service.getCryptocurrencyOutputs(page = page)
        } catch (exception: HttpException) {
            print(exception)
        }
        return emptyList()
    }
}
