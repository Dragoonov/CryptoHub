package com.moonlightbutterfly.cryptohub.repository.retrofit

import com.moonlightbutterfly.cryptohub.BuildConfig
import com.moonlightbutterfly.cryptohub.repository.retrofit.dataobjects.CurrencyOutput
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * The Retrofit service for Nomics (https://nomics.com/docs) API.
 */
interface NomicsService {
    @GET("currencies/ticker")
    suspend fun getCurrencies(@Query("key") apiKey: String = BuildConfig.API_KEY): List<CurrencyOutput>
}