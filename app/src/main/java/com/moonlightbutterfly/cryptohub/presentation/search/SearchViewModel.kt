package com.moonlightbutterfly.cryptohub.presentation.search

import androidx.lifecycle.viewModelScope
import com.moonlightbutterfly.cryptohub.data.common.unpack
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import com.moonlightbutterfly.cryptohub.presentation.core.BaseViewModel
import com.moonlightbutterfly.cryptohub.usecases.AddRecentUseCase
import com.moonlightbutterfly.cryptohub.usecases.ClearRecentsUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetAllCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetRecentsUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveRecentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    getRecentsUseCase: GetRecentsUseCase,
    private val addRecentUseCase: AddRecentUseCase,
    private val removeRecentsUseCase: ClearRecentsUseCase,
    private val getAllCryptoAssetsMarketInfoUseCase: GetAllCryptoAssetsMarketInfoUseCase,
    private val removeRecentUseCase: RemoveRecentUseCase,
) : BaseViewModel() {

    private val _currentSearchQuery = MutableStateFlow("")
    val currentSearchQuery: StateFlow<String> = _currentSearchQuery

    private val _cryptoAssetsResults = MutableStateFlow<List<CryptoAsset>>(emptyList())
    val cryptoAssetsResults: StateFlow<List<CryptoAsset>> = _cryptoAssetsResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val recents = getRecentsUseCase()
        .prepareFlow(CryptoCollection.EMPTY)
        .map {
            it.unpack(CryptoCollection.EMPTY).cryptoAssets
        }

    private var searchPage = 1

    private val allResults: MutableList<CryptoAsset> = mutableListOf()

    private var currentSearchJob: Job? = null

    /**
     * Search is performed only after at least 3 characters have been typed. It is also performed
     * after the delay of [WAITING_TIME_MS], to give user time to type more characters before search.
     */
    fun onQueryChange(query: String) {
        _currentSearchQuery.value = query
        currentSearchJob?.cancel()
        currentSearchJob = viewModelScope.launch {
            if (query.isLongEnough()) {
                delay(WAITING_TIME_MS)
                _isLoading.value = true
                fetchResultsToFilter()
                val results = filterSavedResultsByQuery(query)
                _cryptoAssetsResults.value = results
            } else {
                _cryptoAssetsResults.value = emptyList()
            }
            _isLoading.value = false
        }
    }

    private fun String.isLongEnough() = this.length >= MINIMAL_QUERY_LENGTH

    private suspend fun fetchResultsToFilter() {
        while (searchPage <= THRESHOLD_PAGES_FOR_FILTERING) {
            val newResults = getAllCryptoAssetsMarketInfoUseCase(searchPage).first()
            searchPage += 1
            allResults += newResults.unpack(emptyList()).map { it.asset }
        }
    }

    private fun filterSavedResultsByQuery(query: String): List<CryptoAsset> {
        return allResults.filter {
            it.name.contains(
                query,
                ignoreCase = true
            ) || it.symbol.contains(query, ignoreCase = true)
        }
    }

    fun onResultClicked(cryptoAsset: CryptoAsset) {
        viewModelScope.launch {
            recents.firstOrNull()?.let {
                if (it.contains(cryptoAsset)) {
                    removeRecentUseCase(cryptoAsset).propagateErrors()
                } else if (it.size >= MAX_RECENTS) {
                    removeRecentUseCase(it.first()).propagateErrors()
                }
                addRecentUseCase(cryptoAsset).propagateErrors()
            }
        }
    }

    fun onDeleteRecentsClicked() {
        viewModelScope.launch {
            removeRecentsUseCase().propagateErrors()
        }
    }

    private companion object {
        private const val WAITING_TIME_MS = 1000L
        private const val MINIMAL_QUERY_LENGTH = 3
        private const val THRESHOLD_PAGES_FOR_FILTERING = 10
        private const val MAX_RECENTS = 10
    }
}
