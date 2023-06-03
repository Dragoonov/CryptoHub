package com.moonlightbutterfly.cryptohub.search

import com.moonlightbutterfly.cryptohub.models.CryptoAsset

data class SearchUIState(
    val query: String? = null,
    val searchResults: List<CryptoAsset> = emptyList(),
    val isSearchLoading: Boolean? = null,
    val recents: List<CryptoAsset> = emptyList(),
    val isLoading: Boolean = true,
    val error: Throwable? = null
)
