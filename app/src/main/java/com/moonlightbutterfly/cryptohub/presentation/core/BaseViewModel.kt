package com.moonlightbutterfly.cryptohub.presentation.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.data.common.Error
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

    private val messageFlow = MutableSharedFlow<Error>()
    val errorMessageFlow: Flow<Error> = messageFlow

    fun <T> Flow<Answer<T>>.prepareFlow(initialValue: T): Flow<Answer<T>> {
        return onEach {
            if (it is Answer.Failure) {
                messageFlow.emit(it.error)
                Timber.e(it.error.message)
            }
        }.stateIn(
            initialValue = Answer.Success(initialValue),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(CONFIGURATION_TIMEOUT)
        )
    }

    suspend fun <T> Answer<T>.propagateErrors(): Answer<T> {
        return apply {
            if (this is Answer.Failure) {
                messageFlow.emit(this.error)
                Timber.e(error.message)
            }
        }
    }

    private companion object {
        private const val CONFIGURATION_TIMEOUT = 5000L
    }
}
