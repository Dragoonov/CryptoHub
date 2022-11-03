package com.moonlightbutterfly.cryptohub.models

data class CryptoAsset(
    val name: String = EMPTY_NAME,
    val symbol: String = EMPTY_SYMBOL,
    val logoUrl: String = EMPTY_LOGO_URL,
) {
    companion object {
        val EMPTY = CryptoAsset()
        const val EMPTY_NAME = ""
        const val EMPTY_SYMBOL = ""
        const val EMPTY_LOGO_URL = ""
    }
}
