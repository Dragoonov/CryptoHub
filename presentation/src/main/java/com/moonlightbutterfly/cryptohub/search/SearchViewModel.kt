package com.moonlightbutterfly.cryptohub.search

import androidx.lifecycle.viewModelScope
import com.moonlightbutterfly.cryptohub.core.BaseViewModel
import com.moonlightbutterfly.cryptohub.data.common.getOrNull
import com.moonlightbutterfly.cryptohub.data.common.getOrThrow
import com.moonlightbutterfly.cryptohub.data.common.unpack
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import com.moonlightbutterfly.cryptohub.usecases.AddRecentUseCase
import com.moonlightbutterfly.cryptohub.usecases.ClearRecentsUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetAllCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetRecentsUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveRecentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getRecentsUseCase: GetRecentsUseCase,
    private val addRecentUseCase: AddRecentUseCase,
    private val removeRecentsUseCase: ClearRecentsUseCase,
    private val getAllCryptoAssetsMarketInfoUseCase: GetAllCryptoAssetsMarketInfoUseCase,
    private val removeRecentUseCase: RemoveRecentUseCase,
    initialState: SearchUIState
) : BaseViewModel<SearchIntent, SearchUIState>(initialState) {

    init {
        acceptIntent(SearchIntent.GetData)
    }

    private var searchPage = 1
    private var searchJob: Job? = null
    private val allResults: MutableList<CryptoAsset> = mutableListOf()

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

    /**
     * Search is performed only after at least 3 characters have been typed. It is also performed
     * after the delay of [WAITING_TIME_MS], to give user time to type more characters before search.
     */
    private fun search(query: String): Flow<SearchUIState> {
        val flow = MutableStateFlow(uiState.value.copy(query = query, isSearchLoading = false))
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (query.isLongEnough()) {
                flow.emit(uiState.value.copy(isSearchLoading = true))
                delay(WAITING_TIME_MS)
                fetchResultsToFilter()
                val results = filterSavedResultsByQuery(query)
                flow.emit(uiState.value.copy(searchResults = results, isSearchLoading = false))
            } else {
                flow.emit(uiState.value.copy(searchResults = emptyList(), isSearchLoading = false))
            }
        }
        return flow
    }

    private suspend fun getCurrentRecents() = getRecentsUseCase().first().getOrNull()?.cryptoAssets ?: emptyList()

    private fun deleteRecents(): Flow<SearchUIState> = flow {
        removeRecentsUseCase().getOrThrow()
        emit(
            uiState.value.copy(recents = getCurrentRecents())
        )
    }

    private fun resultPicked(asset: CryptoAsset): Flow<SearchUIState> = flow {
        uiState.value.recents.let {
            if (it.contains(asset)) {
                removeRecentUseCase(asset).getOrThrow()
            } else if (it.size >= MAX_RECENTS) {
                removeRecentUseCase(it.first()).getOrThrow()
            }
            addRecentUseCase(asset)
            emit(
                uiState.value.copy(recents = getCurrentRecents())
            )
        }
    }

    private fun getData(): Flow<SearchUIState> = flow {
        val recents = getRecentsUseCase().first().unpack(CryptoCollection.EMPTY).cryptoAssets
        emit(
            uiState.value.copy(
                query = "",
                searchResults = emptyList(),
                isSearchLoading = false,
                recents = recents,
                isLoading = false,
                error = null
            )
        )
    }

    override fun mapIntent(intent: SearchIntent): Flow<SearchUIState> {
        return when (intent) {
            is SearchIntent.GetData -> getData()
            is SearchIntent.PickResult -> resultPicked(intent.cryptoAsset)
            is SearchIntent.DeleteRecents -> deleteRecents()
            is SearchIntent.Search -> search(intent.query)
        }
    }

    private companion object {
        private const val WAITING_TIME_MS = 1000L
        private const val MINIMAL_QUERY_LENGTH = 3
        private const val THRESHOLD_PAGES_FOR_FILTERING = 10
        private const val MAX_RECENTS = 10
    }
}
