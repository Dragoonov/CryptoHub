package com.moonlightbutterfly.cryptohub.repository.retrofit

import com.moonlightbutterfly.cryptohub.BuildConfig
import com.moonlightbutterfly.cryptohub.CRYPTOCURRENCIES_LOAD_NUMBER_PER_PAGE
import com.moonlightbutterfly.cryptohub.repository.dataobjects.CryptocurrencyOutput
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * The Retrofit service for Nomics (https://nomics.com/docs) API.
 */
interface NomicsService {
    @GET("currencies/ticker")
    suspend fun getCryptocurrencyOutputs(
        @Query("key") apiKey: String = BuildConfig.API_KEY,
        @Query("per-page") itemsPerPage: Int = CRYPTOCURRENCIES_LOAD_NUMBER_PER_PAGE,
        @Query("page") page: Int,
    ): List<CryptocurrencyOutput>
}
