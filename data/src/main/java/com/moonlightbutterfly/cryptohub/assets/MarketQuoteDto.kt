package com.moonlightbutterfly.cryptohub.assets

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the asset market quote returned by the CoinMarketCap API.
 */
@Serializable
internal data class MarketQuoteDto(
    val price: Double,
    @SerialName("volume_24h") val volume24H: Double? = .0,
    @SerialName("volume_change_24h") val volumeChange24H: Double? = .0,
    @SerialName("percent_change_1h") val percentChange1H: Double? = .0,
    @SerialName("percent_change_24h") val percentChange24H: Double? = .0,
    @SerialName("percent_change_7d") val percentChangeWeek: Double? = .0,
    @SerialName("percent_change_30d") val percentChangeMonth: Double? = .0,
    @SerialName("market_cap") val marketCap: Double? = .0,
    @SerialName("market_cap_dominance") val marketCapDominance: Double? = .0,
)
