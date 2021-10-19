package com.moonlightbutterfly.cryptohub.repository.retrofit

import com.moonlightbutterfly.cryptohub.BuildConfig
import com.moonlightbutterfly.cryptohub.CRYPTOASSETS_LOAD_NUMBER_PER_PAGE
import com.moonlightbutterfly.cryptohub.repository.dataobjects.CryptoassetItemOutput
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * The Retrofit service for Nomics (https://nomics.com/docs) API.
 */
interface NomicsService {
    @GET("currencies/ticker")
    suspend fun getCryptoassetOutputs(
        @Query("key") apiKey: String = BuildConfig.API_KEY,
        @Query("per-page") itemsPerPage: Int = CRYPTOASSETS_LOAD_NUMBER_PER_PAGE,
        @Query("page") page: Int,
    ): List<CryptoassetItemOutput>

    @GET("currencies/ticker")
    suspend fun getCryptoassetOutputs(
        @Query("ids") ids: String,
        @Query("key") apiKey: String = BuildConfig.API_KEY,
        @Query("per-page") itemsPerPage: Int = CRYPTOASSETS_LOAD_NUMBER_PER_PAGE,
        @Query("page") page: Int,
    ): List<CryptoassetItemOutput>

    @GET("currencies/ticker")
    suspend fun getCryptoassetOutputs(
        @Query("key") apiKey: String = BuildConfig.API_KEY,
        @Query("ids") id: String,
    ): List<CryptoassetItemOutput>
}
