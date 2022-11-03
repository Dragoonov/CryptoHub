package com.moonlightbutterfly.cryptohub.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.moonlightbutterfly.cryptohub.data.Error
import com.moonlightbutterfly.cryptohub.data.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onEach

abstract class BaseViewModel : ViewModel() {

    private val messageFlow = MutableSharedFlow<Error>()
    val errorMessageFlow: Flow<Error> = messageFlow

    fun <T> Flow<Result<T>>.propagateErrors(): Flow<Result<T>> {
        return onEach {
            if (it is Result.Failure) {
                messageFlow.emit(it.error)
            }
        }
    }

    suspend fun <T> Result<T>.propagateErrors(): Result<T> {
        return apply {
            if (this is Result.Failure) {
                messageFlow.emit(this.error)
            }
        }
    }
}
