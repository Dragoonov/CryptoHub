package com.moonlightbutterfly.cryptohub.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.moonlightbutterfly.cryptohub.data.CryptoAssetsDataSource
import com.moonlightbutterfly.cryptohub.data.CryptoAssetsRepository
import com.moonlightbutterfly.cryptohub.data.LocalPreferencesDataSource
import com.moonlightbutterfly.cryptohub.data.LocalPreferencesRepository
import com.moonlightbutterfly.cryptohub.data.UserConfigurationDataSource
import com.moonlightbutterfly.cryptohub.data.UserConfigurationRepository
import com.moonlightbutterfly.cryptohub.data.UserDataCache
import com.moonlightbutterfly.cryptohub.framework.CoinMarketCapService
import com.moonlightbutterfly.cryptohub.framework.UserDataCacheImpl
import com.moonlightbutterfly.cryptohub.framework.database.CryptoHubDatabase
import com.moonlightbutterfly.cryptohub.framework.database.daos.FavouritesDao
import com.moonlightbutterfly.cryptohub.framework.database.daos.LocalPreferencesDao
import com.moonlightbutterfly.cryptohub.framework.database.daos.RecentsDao
import com.moonlightbutterfly.cryptohub.framework.datasources.CryptoAssetsDataSourceImpl
import com.moonlightbutterfly.cryptohub.framework.datasources.LocalPreferencesDataSourceImpl
import com.moonlightbutterfly.cryptohub.framework.datasources.UserConfigurationLocalDataSourceImpl
import com.moonlightbutterfly.cryptohub.framework.datasources.UserConfigurationRemoteDataSourceImpl
import com.moonlightbutterfly.cryptohub.usecases.GetSignedInUserUseCase
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RepositoryModule {

    private val service by lazy {
        Retrofit.Builder()
            .baseUrl(API_ADDRESS)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(CoinMarketCapService::class.java)
    }

    private val userConfigurationRemoteDataSource = UserConfigurationRemoteDataSourceImpl(Firebase.firestore)

    @Provides
    fun provideCoinMarketCapService(): CoinMarketCapService = service

    @Provides
    fun provideFirestoreDatabase(): FirebaseFirestore = Firebase.firestore

    @Provides
    fun provideCryptoAssetsDataSource(): CryptoAssetsDataSource = CryptoAssetsDataSourceImpl(service)

    @Provides
    fun provideUserConfigurationDataSource(
        favouritesDao: FavouritesDao,
        recentsDao: RecentsDao,
        getSignedInUserUseCase: GetSignedInUserUseCase,
    ): UserConfigurationDataSource {
        val user = getSignedInUserUseCase()
        return if (user != null)
            userConfigurationRemoteDataSource.apply { registerFor(user.userId) }
        else {
            UserConfigurationLocalDataSourceImpl(favouritesDao, recentsDao)
        }
    }

    @Provides
    fun provideLocalPreferencesRepository(
        localPreferencesDataSource: LocalPreferencesDataSource
    ): LocalPreferencesRepository {
        return LocalPreferencesRepository(localPreferencesDataSource)
    }

    @Provides
    fun provideLocalPreferencesDataSource(
        localPreferencesDao: LocalPreferencesDao
    ): LocalPreferencesDataSource {
        return LocalPreferencesDataSourceImpl(localPreferencesDao)
    }

    @Provides
    fun provideLocalPreferencesDao(context: Context): LocalPreferencesDao =
        CryptoHubDatabase.getInstance(context).localPreferencesDao()

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

    @Provides
    @Singleton
    fun provideUserDataCache(): UserDataCache = UserDataCacheImpl(FirebaseAuth.getInstance())

    private companion object {
        private const val API_ADDRESS = "https://pro-api.coinmarketcap.com/"
    }
}
