package com.moonlightbutterfly.cryptohub.search

import com.moonlightbutterfly.cryptohub.models.CryptoAsset

sealed class SearchIntent {
    object GetData : SearchIntent()
    data class PickResult(val cryptoAsset: CryptoAsset) : SearchIntent()
    data class Search(val query: String) : SearchIntent()
    object DeleteRecents : SearchIntent()
}
