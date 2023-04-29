package com.moonlightbutterfly.cryptohub.assets

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the asset market data returned by the CoinMarketCap API.
 */
@Serializable
data class CryptoAssetMarketQuoteDto(
    val name: String? = null,
    val symbol: String? = null,
    @SerialName("circulating_supply")val circulatingSupply: Double? = .0,
    @SerialName("total_supply") val totalSupply: Double? = .0,
    @SerialName("max_supply") val maxSupply: Double? = .0,
    @SerialName("cmc_rank") val rank: Int? = 0,
    @SerialName("quote") val quotes: Map<String, MarketQuoteDto>? = null
)
