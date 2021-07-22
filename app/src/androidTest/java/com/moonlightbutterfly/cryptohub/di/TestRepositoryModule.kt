package com.moonlightbutterfly.cryptohub.di

import com.moonlightbutterfly.cryptohub.repository.CryptoHubExternalRepositoryFakeImpl
import com.moonlightbutterfly.cryptohub.repository.CryptoHubInternalRepositoryFakeImpl
import com.moonlightbutterfly.cryptohub.repository.CryptoHubExternalRepository
import com.moonlightbutterfly.cryptohub.repository.CryptoHubInternalRepository
import dagger.Binds
import dagger.Module

@Module
abstract class TestRepositoryModule {

    @Binds
    abstract fun bindInternalRepository(cryptoHubRepositoryImpl: CryptoHubInternalRepositoryFakeImpl): CryptoHubInternalRepository

    @Binds
    abstract fun bindExternalRepository(cryptocurrenciesRepositoryImpl: CryptoHubExternalRepositoryFakeImpl): CryptoHubExternalRepository

//
//    @Module
//    companion object {
//        @Provides
//        fun provideAppConfigDao(context: Context): AppConfigDao = CryptoHubDatabase.getInstance(context).appConfigDao()
//    }

}