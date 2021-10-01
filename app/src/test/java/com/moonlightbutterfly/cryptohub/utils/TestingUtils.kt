package com.moonlightbutterfly.cryptohub.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeForTesting(block: () -> Unit) {
    val observer: Observer<T> = Observer { }
    observeForever(observer)
    block()
    removeObserver(observer)
}
