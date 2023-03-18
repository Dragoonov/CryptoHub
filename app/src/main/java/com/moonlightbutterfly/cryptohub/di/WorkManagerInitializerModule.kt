package com.moonlightbutterfly.cryptohub.di

import android.content.Context
import androidx.startup.Initializer
import androidx.work.Configuration
import androidx.work.WorkManager
import com.moonlightbutterfly.cryptohub.presentation.core.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkManagerInitializerModule : Initializer<WorkManager> {

    @Provides
    @Singleton
    override fun create(@ApplicationContext context: Context): WorkManager {
        val workerFactory = EntryPoints.get(context, BaseApplication.WorkerFactoryEntryPoint::class.java).getWorkerFactory()
        val configuration = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
        if (!WorkManager.isInitialized()) {
            WorkManager.initialize(context, configuration)
        }
        return WorkManager.getInstance(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
