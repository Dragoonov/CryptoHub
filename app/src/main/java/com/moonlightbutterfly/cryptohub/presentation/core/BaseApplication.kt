package com.moonlightbutterfly.cryptohub.presentation.core

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

open class BaseApplication : Application() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WorkerFactoryEntryPoint {
        fun getWorkerFactory(): HiltWorkerFactory
    }
}
