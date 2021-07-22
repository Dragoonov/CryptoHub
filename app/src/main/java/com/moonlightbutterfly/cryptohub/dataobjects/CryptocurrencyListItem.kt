package com.moonlightbutterfly.cryptohub.dataobjects

data class CryptocurrencyListItem(
    val name: String,
    val symbol: String,
    val logoUrl: String,
    val price: Double,
    val priceChange: Double,
    val marketCap: Double,
)