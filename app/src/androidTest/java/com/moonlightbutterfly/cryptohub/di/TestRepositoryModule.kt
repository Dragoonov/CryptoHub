package com.moonlightbutterfly.cryptohub.di

import com.moonlightbutterfly.cryptohub.repository.CryptoHubExternalRepository
import com.moonlightbutterfly.cryptohub.repository.CryptoHubExternalRepositoryFakeImpl
import com.moonlightbutterfly.cryptohub.repository.CryptoHubInternalRepository
import com.moonlightbutterfly.cryptohub.repository.CryptoHubInternalRepositoryFakeImpl
import dagger.Binds
import dagger.Module

@Module
abstract class TestRepositoryModule {

    @Binds
    abstract fun bindInternalRepository(cryptoHubRepositoryImpl: CryptoHubInternalRepositoryFakeImpl): CryptoHubInternalRepository

    @Binds
    abstract fun bindExternalRepository(cryptoassetsRepositoryImpl: CryptoHubExternalRepositoryFakeImpl): CryptoHubExternalRepository
}
