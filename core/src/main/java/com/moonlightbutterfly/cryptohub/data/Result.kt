package com.moonlightbutterfly.cryptohub.data

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure<T>(val error: Error) : Result<T>()
}

/**
 * Returns [Result.Success.data] if caller is Result.Success or [defaultValue] if caller is Result.Failure
 */
fun <T> Result<T>.unpack(defaultValue: T): T {
    return if (this is Result.Success) {
        this.data
    } else {
        defaultValue
    }
}

fun <T> Result<T>.getOrNull(): T? {
    return if (this is Result.Success) {
        this.data
    } else {
        null
    }
}

fun <T> Result<T>.getOrThrow(): T {
    return (this as Result.Success<T>).data
}
