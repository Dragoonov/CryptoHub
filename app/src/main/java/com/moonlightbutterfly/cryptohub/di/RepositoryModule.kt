package com.moonlightbutterfly.cryptohub.di

import android.content.Context
import com.facebook.login.LoginManager
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.moonlightbutterfly.cryptohub.data.assets.CoinMarketCapService
import com.moonlightbutterfly.cryptohub.data.assets.CryptoAssetsDataSource
import com.moonlightbutterfly.cryptohub.data.assets.CryptoAssetsDataSourceImpl
import com.moonlightbutterfly.cryptohub.data.assets.CryptoAssetsRepository
import com.moonlightbutterfly.cryptohub.data.collections.UserCollectionsLocalDataSource
import com.moonlightbutterfly.cryptohub.data.collections.UserCollectionsLocalDataSourceImpl
import com.moonlightbutterfly.cryptohub.data.collections.UserCollectionsRemoteDataSource
import com.moonlightbutterfly.cryptohub.data.collections.UserCollectionsRemoteDataSourceImpl
import com.moonlightbutterfly.cryptohub.data.collections.UserCollectionsRepository
import com.moonlightbutterfly.cryptohub.data.common.ErrorMapper
import com.moonlightbutterfly.cryptohub.data.common.ErrorMapperImpl
import com.moonlightbutterfly.cryptohub.data.database.CryptoHubDatabase
import com.moonlightbutterfly.cryptohub.data.database.daos.CryptoCollectionsDao
import com.moonlightbutterfly.cryptohub.data.database.daos.LocalPreferencesDao
import com.moonlightbutterfly.cryptohub.data.localpreferences.LocalPreferencesDataSource
import com.moonlightbutterfly.cryptohub.data.localpreferences.LocalPreferencesDataSourceImpl
import com.moonlightbutterfly.cryptohub.data.localpreferences.LocalPreferencesRepository
import com.moonlightbutterfly.cryptohub.data.signin.FirebaseSignInHandler
import com.moonlightbutterfly.cryptohub.data.signin.FirebaseSignInHandlerImpl
import com.moonlightbutterfly.cryptohub.data.user.FirebaseAuthDataProvider
import com.moonlightbutterfly.cryptohub.data.user.UserDataSource
import com.moonlightbutterfly.cryptohub.data.user.UserDataSourceImpl
import com.moonlightbutterfly.cryptohub.data.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindErrorMapper(errorMapperImpl: ErrorMapperImpl): ErrorMapper

    @Binds
    abstract fun bindFirebaseSignInHandler(firebaseSignInHandlerImpl: FirebaseSignInHandlerImpl): FirebaseSignInHandler

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
        fun provideUserCollectionsLocalDataSource(
            cryptoCollectionsDao: CryptoCollectionsDao,
            errorMapper: ErrorMapper,
        ): UserCollectionsLocalDataSource {
            return UserCollectionsLocalDataSourceImpl(cryptoCollectionsDao, errorMapper)
        }

        @Provides
        fun provideUserCollectionsRemoteDataSource(
            firebaseFirestore: FirebaseFirestore,
            errorMapper: ErrorMapper,
        ): UserCollectionsRemoteDataSource {
            return UserCollectionsRemoteDataSourceImpl(
                firebaseFirestore,
                errorMapper
            )
        }

        @Provides
        fun provideLocalPreferencesRepository(
            localPreferencesDataSource: LocalPreferencesDataSource,
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
            userCollectionsLocalDataSource: UserCollectionsLocalDataSource,
            userCollectionsRemoteDataSource: UserCollectionsRemoteDataSource,
            userDataSource: UserDataSource
        ): UserCollectionsRepository {
            return UserCollectionsRepository(userCollectionsRemoteDataSource, userCollectionsLocalDataSource, userDataSource)
        }

        @Provides
        fun provideCryptoAssetsRepository(
            cryptoAssetsDataSource: CryptoAssetsDataSource,
        ): CryptoAssetsRepository {
            return CryptoAssetsRepository(cryptoAssetsDataSource)
        }

        @Provides
        fun provideUserRepository(
            userDataSource: UserDataSource,
        ): UserRepository {
            return UserRepository(userDataSource)
        }

        @Provides
        @ActivityScope
        fun provideUserDataSource(
            firebaseSignInHandler: FirebaseSignInHandler,
            firebaseAuth: FirebaseAuth
        ): UserDataSource {
            return UserDataSourceImpl(firebaseSignInHandler, firebaseAuth)
        }

        @Provides
        fun provideFirebaseAuthResultHandler(context: Context): FirebaseAuthDataProvider {
            return context as FirebaseAuthDataProvider
        }

        @Provides
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

        @Provides
        fun provideLoginManager(): LoginManager = LoginManager.getInstance()

        @Provides
        fun provideAuthProviders(): List<AuthUI.IdpConfig> {
            return arrayListOf(
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.FacebookBuilder().build(),
                AuthUI.IdpConfig.TwitterBuilder().build(),
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.PhoneBuilder().build()
            )
        }
    }
}
