package com.moonlightbutterfly.cryptohub.dataobjects

import com.google.gson.annotations.SerializedName

data class CryptocurrencyItem(
    val name: String = "",
    val symbol: String = "",
    val price: Double = .0,
    val rank: Int = 0,
    @SerializedName("logo_url") val logoUrl: String = "",
    @SerializedName("market_cap") val marketCap: Double = .0,
    @SerializedName("circulating_supply") val circulatingSupply: Double = .0,
    @SerializedName("max_supply") val maxSupply: Double = .0,
    @SerializedName("1d") val dayChanges: IntervalChanges = IntervalChanges.EMPTY,
    @SerializedName("1h") val hourChanges: IntervalChanges = IntervalChanges.EMPTY,
    @SerializedName("7d") val weekChanges: IntervalChanges = IntervalChanges.EMPTY,
    @SerializedName("30d") val monthChanges: IntervalChanges = IntervalChanges.EMPTY,
    @SerializedName("365d") val yearChanges: IntervalChanges = IntervalChanges.EMPTY,
    @SerializedName("ytd") val totalChanges: IntervalChanges = IntervalChanges.EMPTY,
) {
    companion object {
        val EMPTY = CryptocurrencyItem()
    }
}
