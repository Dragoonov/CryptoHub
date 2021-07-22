package com.moonlightbutterfly.cryptohub.repository.retrofit.dataobjects

import com.google.gson.annotations.SerializedName

/**
 * Represents the currency class returned by the Nomics (https://nomics.com/docs) API.
 */
data class CurrencyOutput(
    val currency: String,
    val symbol: String,
    val price: String,
    @SerializedName("logo_url") val logoUrl: String,
    val rank: String
)
