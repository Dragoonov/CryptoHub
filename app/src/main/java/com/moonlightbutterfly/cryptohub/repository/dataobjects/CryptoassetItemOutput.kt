package com.moonlightbutterfly.cryptohub.repository.dataobjects

import com.google.gson.annotations.SerializedName

/**
 * Represents the asset class returned by the Nomics (https://nomics.com/docs) API.
 */
data class CryptoassetItemOutput(
    val name: String? = null,
    val symbol: String? = null,
    val price: String? = null,
    val rank: String? = null,
    @SerializedName("logo_url") val logoUrl: String? = null,
    @SerializedName("market_cap") val marketCap: String? = null,
    @SerializedName("circulating_supply") val circulatingSupply: String? = null,
    @SerializedName("max_supply") val maxSupply: String? = null,
    @SerializedName("1d") val dayChanges: IntervalChangesOutput? = null,
    @SerializedName("1h") val hourChanges: IntervalChangesOutput? = null,
    @SerializedName("7d") val weekChanges: IntervalChangesOutput? = null,
    @SerializedName("30d") val monthChanges: IntervalChangesOutput? = null,
    @SerializedName("365d") val yearChanges: IntervalChangesOutput? = null,
    @SerializedName("ytd") val totalChanges: IntervalChangesOutput? = null,
) {
    companion object {
        val EMPTY = CryptoassetItemOutput()
    }
}
