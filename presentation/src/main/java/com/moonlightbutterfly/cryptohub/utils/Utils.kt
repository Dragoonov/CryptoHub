package com.moonlightbutterfly.cryptohub.utils

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.moonlightbutterfly.cryptohub.models.User
import com.moonlightbutterfly.cryptohub.presentation.R

@SuppressWarnings("MagicNumber")
fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}

fun Double.toStringAbbr(context: Context): String = when {
    this > BILLION ->
        "${this.div(BILLION).round(2)} ${context.getString(R.string.billion_abbr)}"
    this > MILLION ->
        "${this.div(MILLION).round(2)} ${context.getString(R.string.million_abbr)}"
    else -> this.round(2).toString()
}

fun FirebaseUser.toUser(): User {
    return User(userId = this.uid, name = this.displayName ?: "", email = this.email ?: "")
}

private const val BILLION = 1_000_000_000.0
private const val MILLION = 1_000_000.0
