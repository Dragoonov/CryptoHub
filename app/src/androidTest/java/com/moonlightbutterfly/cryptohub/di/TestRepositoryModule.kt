package com.moonlightbutterfly.cryptohub.di

import com.moonlightbutterfly.cryptohub.data.CryptoAssetsDataSource
import com.moonlightbutterfly.cryptohub.data.CryptoAssetsRepository
import com.moonlightbutterfly.cryptohub.data.UserConfigurationDataSource
import com.moonlightbutterfly.cryptohub.data.UserConfigurationRepository
import com.moonlightbutterfly.cryptohub.repository.FakeCryptoAssetsDataSourceImpl
import com.moonlightbutterfly.cryptohub.repository.FakeUserConfigurationDataSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestRepositoryModule {

    @Provides
    fun provideCryptoAssetsDataSource(): CryptoAssetsDataSource {
        return FakeCryptoAssetsDataSourceImpl()
    }

    @Provides
    @Singleton
    fun provideUserConfigurationDataSource(): UserConfigurationDataSource {
        return FakeUserConfigurationDataSourceImpl()
    }

    @Provides
    fun provideUserConfigurationRepository(
        userConfigurationDataSource: UserConfigurationDataSource
    ): UserConfigurationRepository {
        return UserConfigurationRepository(userConfigurationDataSource)
    }

    @Provides
    fun provideCryptoAssetsRepository(
        cryptoAssetsDataSource: CryptoAssetsDataSource
    ): CryptoAssetsRepository {
        return CryptoAssetsRepository(cryptoAssetsDataSource)
    }
}
