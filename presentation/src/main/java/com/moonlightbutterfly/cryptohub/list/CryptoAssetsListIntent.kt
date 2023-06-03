package com.moonlightbutterfly.cryptohub.list

import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo

sealed class CryptoAssetsListIntent {
    object GetData : CryptoAssetsListIntent()
    data class AddToFavourites(val asset: CryptoAssetMarketInfo) : CryptoAssetsListIntent()
    data class RemoveFromFavourites(val asset: CryptoAssetMarketInfo) : CryptoAssetsListIntent()
}
