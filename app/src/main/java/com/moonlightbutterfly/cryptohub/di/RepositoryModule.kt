package com.moonlightbutterfly.cryptohub.di

import android.content.Context
import com.moonlightbutterfly.cryptohub.data.CryptoAssetsDataSource
import com.moonlightbutterfly.cryptohub.data.CryptoAssetsRepository
import com.moonlightbutterfly.cryptohub.data.UserConfigurationDataSource
import com.moonlightbutterfly.cryptohub.data.UserConfigurationRepository
import com.moonlightbutterfly.cryptohub.framework.CryptoAssetsDataSourceImpl
import com.moonlightbutterfly.cryptohub.framework.UserConfigurationDataSourceImpl
import com.moonlightbutterfly.cryptohub.framework.database.CryptoHubDatabase
import com.moonlightbutterfly.cryptohub.framework.database.daos.FavouritesDao
import com.moonlightbutterfly.cryptohub.framework.database.daos.RecentsDao
import com.moonlightbutterfly.cryptohub.framework.database.daos.UserSettingsDao
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideCryptoAssetsDataSource(): CryptoAssetsDataSource = CryptoAssetsDataSourceImpl()

    @Provides
    fun provideUserConfigurationDataSource(
        userSettingsDao: UserSettingsDao,
        favouritesDao: FavouritesDao,
        recentsDao: RecentsDao,
    ): UserConfigurationDataSource {
        return UserConfigurationDataSourceImpl(userSettingsDao, favouritesDao, recentsDao)
    }

    @Provides
    fun provideUserSettingsDao(context: Context): UserSettingsDao =
        CryptoHubDatabase.getInstance(context).userSettingsDao()

    @Provides
    fun provideFavouritesDao(context: Context): FavouritesDao =
        CryptoHubDatabase.getInstance(context).favouritesDao()

    @Provides
    fun provideRecentsDao(context: Context): RecentsDao =
        CryptoHubDatabase.getInstance(context).recentsDao()

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
