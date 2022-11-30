package com.moonlightbutterfly.cryptohub.data.assets

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CoinMarketCapService {
    @GET("v1/cryptocurrency/info")
    suspend fun getMetadata(
        @Header("X-CMC_PRO_API_KEY") apiKey: String,
        @Query("symbol") symbols: String,
    ): GeneralMetadataDto

    @GET("v1/cryptocurrency/quotes/latest")
    suspend fun getMarketInfo(
        @Header("X-CMC_PRO_API_KEY") apiKey: String,
        @Query("symbol") symbols: String,
    ): GeneralMarketQuoteDto

    @GET("/v1/cryptocurrency/listings/latest")
    suspend fun getListings(
        @Header("X-CMC_PRO_API_KEY") apiKey: String,
        @Query("start") start: Int,
        @Query("limit") limit: Int,
    ): GeneralListingDto
}
