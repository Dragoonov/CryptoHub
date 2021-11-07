package com.moonlightbutterfly.cryptohub.utils

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}

fun Double.toStringAbbr(): String = when {
    this > 1_000_000_000.0 -> "${this.div(1_000_000_000.0).round(2)} bln"
    this > 1_000_000.0 -> "${this.div(1_000_000.0).round(2)} mln"
    else -> this.round(2).toString()
}
