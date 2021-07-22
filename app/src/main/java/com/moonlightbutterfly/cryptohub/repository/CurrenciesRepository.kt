package com.moonlightbutterfly.cryptohub.repository

import com.google.gson.GsonBuilder
import com.moonlightbutterfly.cryptohub.repository.retrofit.NomicsService
import com.moonlightbutterfly.cryptohub.repository.retrofit.dataobjects.CurrencyOutput
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Repository returning the info about cryptocurrencies, using the Nomics (https://nomics.com/docs) API.
 */
class CurrenciesRepository {
    private val service by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.nomics.com/v1/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(NomicsService::class.java)
    }

    /**
     * Returns the list of cryptocurrencies.
     */
    suspend fun getCurrencies(): List<CurrencyOutput> = service.getCurrencies()
}
