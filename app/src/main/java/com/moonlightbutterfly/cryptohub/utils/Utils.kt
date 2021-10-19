package com.moonlightbutterfly.cryptohub.utils

import com.moonlightbutterfly.cryptohub.dataobjects.CryptoassetItem
import com.moonlightbutterfly.cryptohub.dataobjects.IntervalChanges
import com.moonlightbutterfly.cryptohub.repository.dataobjects.CryptoassetItemOutput
import com.moonlightbutterfly.cryptohub.repository.dataobjects.IntervalChangesOutput

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}

fun CryptoassetItemOutput.toCryptoassetItem() =
    CryptoassetItem(
        name = name ?: "",
        symbol = symbol ?: "",
        logoUrl = logoUrl ?: "",
        price = price?.toDouble()?.round(2) ?: .00,
        rank = rank?.toInt() ?: 1,
        marketCap = marketCap?.toDouble()?.round(2) ?: .00,
        circulatingSupply = circulatingSupply?.toDouble()?.round(2) ?: .00,
        maxSupply = maxSupply?.toDouble()?.round(2) ?: .00,
        dayChanges = dayChanges?.toIntervalChanges() ?: IntervalChanges.EMPTY
    )

fun IntervalChangesOutput.toIntervalChanges() =
    IntervalChanges(
        priceChange = priceChange?.toDouble()?.round(2) ?: .00,
        priceChangePercent = priceChangePercent?.toDouble()?.times(100)?.round(2) ?: .00,
        volume = volume?.toDouble()?.round(2) ?: .00,
        volumeChangePct = volumeChangePct?.toDouble()?.times(100)?.round(2) ?: .00,
        marketCapChangePct = marketCapChangePct?.toDouble()?.times(100)?.round(2) ?: .00,
    )

fun Double.toStringAbbr(): String = when {
    this > 1_000_000_000.0 -> "${this.div(1_000_000_000.0).round(2)} bln"
    this > 1_000_000.0 -> "${this.div(1_000_000.0).round(2)} mln"
    else -> this.round(2).toString()
}
