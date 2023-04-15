package com.moonlightbutterfly.cryptohub.presentation.core

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import com.moonlightbutterfly.cryptohub.BuildConfig
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import timber.log.Timber

open class BaseApplication : Application() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WorkerFactoryEntryPoint {
        fun getWorkerFactory(): HiltWorkerFactory
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
