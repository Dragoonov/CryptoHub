package com.moonlightbutterfly.cryptohub.data.assets

import kotlinx.serialization.Serializable

@Serializable
data class GeneralListingDto(val data: List<CryptoAssetMarketQuoteDto>)
@Serializable
data class GeneralMarketQuoteDto(val data: Map<String, CryptoAssetMarketQuoteDto>)
@Serializable
data class GeneralMetadataDto(val data: Map<String, CryptoAssetMetadataDto>)
