package com.moonlightbutterfly.cryptohub.framework.dtos

/**
 * Represents the asset metadata returned by the CoinMarketCap API.
 */
data class CryptoAssetMetadataDto(
    val name: String? = null,
    val symbol: String? = null,
    val logo: String? = null,
    val description: String? = null
)
