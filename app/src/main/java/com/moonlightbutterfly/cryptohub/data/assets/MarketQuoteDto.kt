package com.moonlightbutterfly.cryptohub.data.assets

import com.google.gson.annotations.SerializedName

/**
 * Represents the asset market quote returned by the CoinMarketCap API.
 */
data class MarketQuoteDto(
    val price: Double,
    @SerializedName("volume_24h") val volume24H: Double? = .0,
    @SerializedName("volume_change_24h") val volumeChange24H: Double? = .0,
    @SerializedName("percent_change_1h") val percentChange1H: Double? = .0,
    @SerializedName("percent_change_24h") val percentChange24H: Double? = .0,
    @SerializedName("percent_change_7d") val percentChangeWeek: Double? = .0,
    @SerializedName("percent_change_30d") val percentChangeMonth: Double? = .0,
    @SerializedName("market_cap") val marketCap: Double? = .0,
    @SerializedName("market_cap_dominance") val marketCapDominance: Double? = .0,
)
