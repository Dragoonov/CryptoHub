package com.moonlightbutterfly.cryptohub.data.common

sealed class Answer<T> {
    data class Success<T>(val data: T) : Answer<T>()
    data class Failure<T>(val error: Error) : Answer<T>()
}

/**
 * Returns [Answer.Success.data] if caller is Result.Success or [defaultValue] if caller is Result.Failure
 */
fun <T> Answer<T>.unpack(defaultValue: T): T {
    return if (this is Answer.Success) {
        this.data
    } else {
        defaultValue
    }
}

/**
 * Returns [Answer.Success.data] if caller is Result.Success or null if caller is Result.Failure
 */
fun <T> Answer<T>.getOrNull(): T? {
    return if (this is Answer.Success) {
        this.data
    } else {
        null
    }
}

/**
 * Returns [Answer.Success.data] if caller is Result.Success or throws [ClassCastException] if caller is Result.Failure
 */
fun <T> Answer<T>.getOrThrow(): T {
    return (this as Answer.Success<T>).data
}
