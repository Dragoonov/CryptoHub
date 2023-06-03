package com.moonlightbutterfly.cryptohub.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel<INTENT, UI_STATE>(initialValue: UI_STATE) : ViewModel() {

    private val _uiState = MutableStateFlow(initialValue)
    val uiState = _uiState.asStateFlow()

    fun acceptIntent(intent: INTENT) {
        viewModelScope.launch {
            mapIntent(intent)
                .catch { Timber.e(it) }
                .collect {
                    _uiState.emit(it)
                }
        }
    }
    protected abstract fun mapIntent(intent: INTENT): Flow<UI_STATE>
}
