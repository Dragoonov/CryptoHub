package com.moonlightbutterfly.cryptohub.repository.dataobjects

import com.google.gson.annotations.SerializedName

/**
 * Represents the class containing daily changes info returned by the Nomics (https://nomics.com/docs) API.
 */
data class DayChangesOutput(
    @SerializedName("price_change_pct") val priceChangePercent: String?
)
