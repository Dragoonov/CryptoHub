package com.moonlightbutterfly.cryptohub.assets

import kotlinx.serialization.Serializable

/**
 * Represents the asset metadata returned by the CoinMarketCap API.
 */
@Serializable
internal data class CryptoAssetMetadataDto(
    val name: String? = null,
    val symbol: String? = null,
    val logo: String? = null,
    val description: String? = null
)
