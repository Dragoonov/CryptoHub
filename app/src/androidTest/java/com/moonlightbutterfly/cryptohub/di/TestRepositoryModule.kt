package com.moonlightbutterfly.cryptohub.di

import com.google.firebase.auth.FirebaseAuth
import com.moonlightbutterfly.cryptohub.data.CryptoAssetsDataSource
import com.moonlightbutterfly.cryptohub.data.CryptoAssetsRepository
import com.moonlightbutterfly.cryptohub.data.LocalPreferencesDataSource
import com.moonlightbutterfly.cryptohub.data.LocalPreferencesRepository
import com.moonlightbutterfly.cryptohub.data.UserConfigurationDataSource
import com.moonlightbutterfly.cryptohub.data.UserConfigurationRepository
import com.moonlightbutterfly.cryptohub.data.UserDataCache
import com.moonlightbutterfly.cryptohub.framework.UserDataCacheImpl
import com.moonlightbutterfly.cryptohub.repository.FakeCryptoAssetsDataSourceImpl
import com.moonlightbutterfly.cryptohub.repository.FakeLocalPreferencesDataSourceImpl
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
    fun provideLocalPreferencesRepository(
        localPreferencesDataSource: LocalPreferencesDataSource
    ): LocalPreferencesRepository {
        return LocalPreferencesRepository(localPreferencesDataSource)
    }

    @Provides
    fun provideCryptoAssetsRepository(
        cryptoAssetsDataSource: CryptoAssetsDataSource
    ): CryptoAssetsRepository {
        return CryptoAssetsRepository(cryptoAssetsDataSource)
    }

    @Provides
    @Singleton
    fun provideLocalPreferencesDataSource(): LocalPreferencesDataSource =
        FakeLocalPreferencesDataSourceImpl()

    @Provides
    @Singleton
    fun provideUserDataCache(): UserDataCache = UserDataCacheImpl(FirebaseAuth.getInstance())
}
