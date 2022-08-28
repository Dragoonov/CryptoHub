package com.moonlightbutterfly.cryptohub.di

import com.moonlightbutterfly.cryptohub.data.CryptoAssetsDataSource
import com.moonlightbutterfly.cryptohub.data.CryptoAssetsRepository
import com.moonlightbutterfly.cryptohub.data.LocalPreferencesDataSource
import com.moonlightbutterfly.cryptohub.data.LocalPreferencesRepository
import com.moonlightbutterfly.cryptohub.data.UserCollectionsDataSource
import com.moonlightbutterfly.cryptohub.data.UserCollectionsRepository
import com.moonlightbutterfly.cryptohub.data.UserDataCache
import com.moonlightbutterfly.cryptohub.framework.UserDataCacheImpl
import com.moonlightbutterfly.cryptohub.repository.FakeCryptoAssetsDataSourceImpl
import com.moonlightbutterfly.cryptohub.repository.FakeLocalPreferencesDataSourceImpl
import com.moonlightbutterfly.cryptohub.repository.FakeUserCollectionsDataSourceImpl
import dagger.Module
import dagger.Provides

@Module
class TestRepositoryModule {

    @Provides
    fun provideCryptoAssetsDataSource(): CryptoAssetsDataSource {
        return FakeCryptoAssetsDataSourceImpl()
    }

    @Provides
    @ActivityScope
    fun provideUserConfigurationDataSource(): UserCollectionsDataSource {
        return FakeUserCollectionsDataSourceImpl()
    }

    @Provides
    fun provideUserConfigurationRepository(
        userCollectionsDataSource: UserCollectionsDataSource
    ): UserCollectionsRepository {
        return UserCollectionsRepository(userCollectionsDataSource)
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
    @ActivityScope
    fun provideLocalPreferencesDataSource(): LocalPreferencesDataSource =
        FakeLocalPreferencesDataSourceImpl()

    @Provides
    @ActivityScope
    fun provideUserDataCache(): UserDataCache = UserDataCacheImpl
}
