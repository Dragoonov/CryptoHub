package com.moonlightbutterfly.cryptohub.assets

import kotlinx.serialization.Serializable

@Serializable
internal data class GeneralListingDto(val data: List<CryptoAssetMarketQuoteDto>)
@Serializable
internal data class GeneralMarketQuoteDto(val data: Map<String, CryptoAssetMarketQuoteDto>)
@Serializable
internal data class GeneralMetadataDto(val data: Map<String, CryptoAssetMetadataDto>)
