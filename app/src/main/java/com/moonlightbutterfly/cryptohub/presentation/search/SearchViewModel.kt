package com.moonlightbutterfly.cryptohub.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
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
import kotlinx.coroutines.flow.first
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

    private val _currentSearchQuery = MutableLiveData("")
    val currentSearchQuery: LiveData<String> = _currentSearchQuery

    private val _cryptoAssetsResults = MutableLiveData<List<CryptoAsset>>()
    val cryptoAssetsResults: LiveData<List<CryptoAsset>> = _cryptoAssetsResults

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    val recents = getRecentsUseCase()
        .propagateErrors()
        .map {
            it.unpack(CryptoCollection.EMPTY).cryptoAssets
        }
        .asLiveData()

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
                _isLoading.postValue(true)
                fetchResultsToFilter()
                val results = filterSavedResultsByQuery(query)
                _cryptoAssetsResults.postValue(results)
            } else {
                _cryptoAssetsResults.postValue(emptyList())
            }
            _isLoading.postValue(false)
        }
    }

    private fun String.isLongEnough() = this.length >= 3

    private suspend fun fetchResultsToFilter() {
        while (searchPage <= 10) {
            val newResults = getAllCryptoAssetsMarketInfoUseCase(searchPage).propagateErrors().first()
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
        recents.value?.let {
            viewModelScope.launch {
                if (it.contains(cryptoAsset)) {
                    removeRecentUseCase(cryptoAsset).propagateErrors()
                } else if (it.size >= 10) {
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
    }
}
