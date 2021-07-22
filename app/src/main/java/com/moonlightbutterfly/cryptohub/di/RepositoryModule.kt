package com.moonlightbutterfly.cryptohub.di

import android.content.Context
import com.moonlightbutterfly.cryptohub.repository.CryptoHubExternalRepository
import com.moonlightbutterfly.cryptohub.repository.CryptoHubExternalRepositoryImpl
import com.moonlightbutterfly.cryptohub.repository.CryptoHubInternalRepository
import com.moonlightbutterfly.cryptohub.repository.CryptoHubInternalRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import database.CryptoHubDatabase
import database.daos.AppConfigDao

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindInternalRepository(cryptoHubRepositoryImpl: CryptoHubInternalRepositoryImpl): CryptoHubInternalRepository

    @Binds
    abstract fun bindExternalRepository(cryptocurrenciesRepositoryImpl: CryptoHubExternalRepositoryImpl): CryptoHubExternalRepository

    @Module
    companion object {
        @Provides
        fun provideAppConfigDao(context: Context): AppConfigDao = CryptoHubDatabase.getInstance(context).appConfigDao()
    }

}