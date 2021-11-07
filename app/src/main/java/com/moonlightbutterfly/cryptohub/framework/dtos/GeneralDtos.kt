package com.moonlightbutterfly.cryptohub.framework.dtos

data class GeneralListingDto(val data: List<CryptoAssetMarketQuoteDto>)
data class GeneralMarketQuoteDto(val data: Map<String, CryptoAssetMarketQuoteDto>)
data class GeneralMetadataDto(val data: Map<String, CryptoAssetMetadataDto>)
