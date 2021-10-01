package com.moonlightbutterfly.cryptohub.repository.dataobjects

import com.google.gson.annotations.SerializedName

/**
 * Represents the class containing daily changes info returned by the Nomics (https://nomics.com/docs) API.
 */
data class IntervalChangesOutput(
    @SerializedName("price_change_pct") val priceChangePercent: String? = null,
    @SerializedName("price_change") val priceChange: String? = null,
    @SerializedName("volume") val volume: String? = null,
    @SerializedName("volume_change_pct") val volumeChangePct: String? = null,
    @SerializedName("market_cap_change_pct") val marketCapChangePct: String? = null,

)
