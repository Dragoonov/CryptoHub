package com.moonlightbutterfly.cryptohub.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonlightbutterfly.cryptohub.data.common.Answer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

    fun <T> Flow<Answer<T>>.prepareFlow(initialValue: T): Flow<Answer<T>> {
        return onEach {
            if (it is Answer.Failure) {
                Timber.e(it.error.message)
            }
        }.stateIn(
            initialValue = Answer.Success(initialValue),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(CONFIGURATION_TIMEOUT)
        )
    }

    fun <T> Answer<T>.propagateErrors(): Answer<T> {
        return also {
            if (this is Answer.Failure) {
                Timber.e(error.message)
            }
        }
    }

    private companion object {
        private const val CONFIGURATION_TIMEOUT = 5000L
    }
}
