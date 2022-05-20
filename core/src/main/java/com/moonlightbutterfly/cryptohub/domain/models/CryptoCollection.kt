package com.moonlightbutterfly.cryptohub.domain.models

data class CryptoCollection(
    val name: String = EMPTY_NAME,
    val cryptoAssets: List<CryptoAsset> = listOf()
) {
    companion object {
        val EMPTY = CryptoCollection()
        const val EMPTY_NAME = ""
    }
}
