package com.moonlightbutterfly.cryptohub.domain.models

data class CryptoAssetMarketInfo(
    val asset: CryptoAsset = CryptoAsset(),
    val price: Double = EMPTY_PRICE,
    val rank: Int = EMPTY_RANK,
    val marketCap: Double = EMPTY_MARKET_CAP,
    val circulatingSupply: Double = EMPTY_CIRCULATING_SUPPLY,
    val maxSupply: Double = EMPTY_MAX_SUPPLY,
    val volume24H: Double = EMPTY_VOLUME_24H,
    val volumeChange24H: Double = EMPTY_VOLUME_CHANGE_24H,
    val volumeChangePct24H: Double = EMPTY_VOLUME_CHANGE_PCT_24H,
    val description: String = EMPTY_DESCRIPTION
) {
    companion object {
        val EMPTY = CryptoAssetMarketInfo()
        const val EMPTY_PRICE = .0
        const val EMPTY_RANK = 0
        const val EMPTY_MARKET_CAP = .0
        const val EMPTY_CIRCULATING_SUPPLY = .0
        const val EMPTY_MAX_SUPPLY = .0
        const val EMPTY_VOLUME_24H = .0
        const val EMPTY_VOLUME_CHANGE_24H = .0
        const val EMPTY_VOLUME_CHANGE_PCT_24H = .0
        const val EMPTY_DESCRIPTION = ""
    }
}
