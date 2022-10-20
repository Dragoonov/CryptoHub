package com.moonlightbutterfly.cryptohub.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.moonlightbutterfly.cryptohub.data.CryptoAssetsDataSource
import com.moonlightbutterfly.cryptohub.data.CryptoAssetsRepository
import com.moonlightbutterfly.cryptohub.data.ErrorMapper
import com.moonlightbutterfly.cryptohub.data.LocalPreferencesDataSource
import com.moonlightbutterfly.cryptohub.data.LocalPreferencesRepository
import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.data.UserCollectionsDataSource
import com.moonlightbutterfly.cryptohub.data.UserCollectionsRepository
import com.moonlightbutterfly.cryptohub.data.UserDataCache
import com.moonlightbutterfly.cryptohub.framework.CoinMarketCapService
import com.moonlightbutterfly.cryptohub.framework.UserDataCacheImpl
import com.moonlightbutterfly.cryptohub.framework.database.CryptoHubDatabase
import com.moonlightbutterfly.cryptohub.framework.database.daos.CryptoCollectionsDao
import com.moonlightbutterfly.cryptohub.framework.database.daos.LocalPreferencesDao
import com.moonlightbutterfly.cryptohub.framework.datasources.CryptoAssetsDataSourceImpl
import com.moonlightbutterfly.cryptohub.framework.datasources.ErrorMapperImpl
import com.moonlightbutterfly.cryptohub.framework.datasources.LocalPreferencesDataSourceImpl
import com.moonlightbutterfly.cryptohub.framework.datasources.UserCollectionsLocalDataSourceImpl
import com.moonlightbutterfly.cryptohub.framework.datasources.UserCollectionsRemoteDataSourceImpl
import com.moonlightbutterfly.cryptohub.usecases.GetSignedInUserUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindErrorMapper(errorMapperImpl: ErrorMapperImpl): ErrorMapper

    @Module
    companion object {

        private val service by lazy {
            Retrofit.Builder()
                .baseUrl(API_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build().create(CoinMarketCapService::class.java)
        }

        private const val API_ADDRESS = "https://pro-api.coinmarketcap.com/"

        @Provides
        fun provideCoinMarketCapService(): CoinMarketCapService = service

        @Provides
        fun provideFirestoreDatabase(): FirebaseFirestore = Firebase.firestore

        @Provides
        fun provideCryptoAssetsDataSource(
            errorMapper: ErrorMapper,
        ): CryptoAssetsDataSource = CryptoAssetsDataSourceImpl(service, errorMapper)

        @Provides
        fun provideUserCollectionsDataSource(
            cryptoCollectionsDao: CryptoCollectionsDao,
            firebaseFirestore: FirebaseFirestore,
            getSignedInUserUseCase: GetSignedInUserUseCase,
            errorMapper: ErrorMapper,
        ): UserCollectionsDataSource {
            val userSignedIn = getSignedInUserUseCase()
            return if (userSignedIn is Result.Success) {
                UserCollectionsRemoteDataSourceImpl(firebaseFirestore, userSignedIn.data.userId, errorMapper)
            } else {
                UserCollectionsLocalDataSourceImpl(cryptoCollectionsDao, errorMapper)
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
            localPreferencesDao: LocalPreferencesDao,
            errorMapper: ErrorMapper,
        ): LocalPreferencesDataSource {
            return LocalPreferencesDataSourceImpl(localPreferencesDao, errorMapper)
        }

        @Provides
        fun provideLocalPreferencesDao(context: Context): LocalPreferencesDao =
            CryptoHubDatabase.getInstance(context).localPreferencesDao()

        @Provides
        fun provideCryptoCollectionsDao(context: Context): CryptoCollectionsDao =
            CryptoHubDatabase.getInstance(context).cryptoCollectionsDao()

        @Provides
        fun provideUserCollectionsRepository(
            userCollectionsDataSource: UserCollectionsDataSource
        ): UserCollectionsRepository {
            return UserCollectionsRepository(userCollectionsDataSource)
        }

        @Provides
        fun provideCryptoAssetsRepository(
            cryptoAssetsDataSource: CryptoAssetsDataSource
        ): CryptoAssetsRepository {
            return CryptoAssetsRepository(cryptoAssetsDataSource)
        }

        @Provides
        @ActivityScope
        fun provideUserDataCache(): UserDataCache = UserDataCacheImpl

    }
}
