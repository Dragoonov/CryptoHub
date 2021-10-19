package com.moonlightbutterfly.cryptohub.di

import android.content.Context
import com.moonlightbutterfly.cryptohub.database.CryptoHubDatabase
import com.moonlightbutterfly.cryptohub.database.daos.AppConfigDao
import com.moonlightbutterfly.cryptohub.database.daos.FavouritesDao
import com.moonlightbutterfly.cryptohub.repository.CryptoHubExternalRepository
import com.moonlightbutterfly.cryptohub.repository.CryptoHubExternalRepositoryImpl
import com.moonlightbutterfly.cryptohub.repository.CryptoHubInternalRepository
import com.moonlightbutterfly.cryptohub.repository.CryptoHubInternalRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindInternalRepository(cryptoHubRepositoryImpl: CryptoHubInternalRepositoryImpl): CryptoHubInternalRepository

    @Binds
    abstract fun bindExternalRepository(cryptoassetsRepositoryImpl: CryptoHubExternalRepositoryImpl): CryptoHubExternalRepository

    companion object {

        @Provides
        fun provideAppConfigDao(context: Context): AppConfigDao = CryptoHubDatabase.getInstance(context).appConfigDao()

        @Provides
        fun provideFavouritesDao(context: Context): FavouritesDao = CryptoHubDatabase.getInstance(context).favouritesDao()
    }
}
