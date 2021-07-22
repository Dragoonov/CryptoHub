package com.moonlightbutterfly.cryptohub.repository.retrofit.dataobjects

import com.google.gson.annotations.SerializedName

/**
 * Represents the currency class returned by the Nomics (https://nomics.com/docs) API.
 */
data class CryptocurrencyOutput(
    val name: String? = null,
    val symbol: String? = null,
    val price: String? = null,
    @SerializedName("logo_url") val logoUrl: String? = null,
    @SerializedName("market_cap") val marketCap: String? = null,
    @SerializedName("1d") val dayChangesOutput: DayChangesOutput? = null
)
