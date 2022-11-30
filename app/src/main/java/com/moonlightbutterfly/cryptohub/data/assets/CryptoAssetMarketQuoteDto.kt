package com.moonlightbutterfly.cryptohub.data.assets

import com.google.gson.annotations.SerializedName

/**
 * Represents the asset market data returned by the CoinMarketCap API.
 */
data class CryptoAssetMarketQuoteDto(
    val name: String? = null,
    val symbol: String? = null,
    @SerializedName("circulating_supply")val circulatingSupply: Double? = .0,
    @SerializedName("total_supply") val totalSupply: Double? = .0,
    @SerializedName("max_supply") val maxSupply: Double? = .0,
    @SerializedName("cmc_rank") val rank: Int? = 0,
    @SerializedName("quote") val quotes: Map<String, MarketQuoteDto>? = null
)
