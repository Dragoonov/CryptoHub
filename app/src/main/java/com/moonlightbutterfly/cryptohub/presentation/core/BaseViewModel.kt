package com.moonlightbutterfly.cryptohub.presentation.core

import android.util.Log
import androidx.lifecycle.ViewModel
import com.moonlightbutterfly.cryptohub.data.common.Error
import com.moonlightbutterfly.cryptohub.data.common.Result
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
                Log.e(this::class.java.name, it.error.message)
            }
        }
    }

    suspend fun <T> Result<T>.propagateErrors(): Result<T> {
        return apply {
            if (this is Result.Failure) {
                messageFlow.emit(this.error)
                Log.e(this::class.java.name, error.message)
            }
        }
    }
}
