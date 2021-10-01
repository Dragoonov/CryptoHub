package com.moonlightbutterfly.cryptohub.dataobjects

import com.google.gson.annotations.SerializedName

/**
 * Represents the class containing daily changes info returned by the Nomics (https://nomics.com/docs) API.
 */
data class IntervalChanges(
    @SerializedName("price_change_pct") val priceChangePercent: Double = .0,
    @SerializedName("price_change") val priceChange: Double = .0,
    @SerializedName("volume") val volume: Double = .0,
    @SerializedName("volume_change_pct") val volumeChangePct: Double = .0,
    @SerializedName("market_cap_change_pct") val marketCapChangePct: Double = .0,
) {
    companion object {
        val EMPTY = IntervalChanges()
    }
}
