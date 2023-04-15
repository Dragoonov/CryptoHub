package com.moonlightbutterfly.cryptohub.di

import android.content.Context
import androidx.work.WorkManager
import com.facebook.login.LoginManager
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
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
import com.moonlightbutterfly.cryptohub.data.notifications.Notifier
import com.moonlightbutterfly.cryptohub.data.notifications.NotifierImpl
import com.moonlightbutterfly.cryptohub.data.signin.FirebaseSignInHandler
import com.moonlightbutterfly.cryptohub.data.signin.FirebaseSignInHandlerImpl
import com.moonlightbutterfly.cryptohub.data.user.FirebaseAuthDataProvider
import com.moonlightbutterfly.cryptohub.data.user.FirebaseAuthDataProviderImpl
import com.moonlightbutterfly.cryptohub.data.user.UserDataSource
import com.moonlightbutterfly.cryptohub.data.user.UserDataSourceImpl
import com.moonlightbutterfly.cryptohub.data.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindFirebaseAuthDataProvider(provider: FirebaseAuthDataProviderImpl): FirebaseAuthDataProvider

    @Binds
    abstract fun bindErrorMapper(errorMapperImpl: ErrorMapperImpl): ErrorMapper

    @Binds
    abstract fun bindFirebaseSignInHandler(firebaseSignInHandlerImpl: FirebaseSignInHandlerImpl): FirebaseSignInHandler

    @Binds
    abstract fun bindUserDataSource(userDataSourceImpl: UserDataSourceImpl): UserDataSource

    @Binds
    abstract fun bindsCryptoAssetsDataSource(impl: CryptoAssetsDataSourceImpl): CryptoAssetsDataSource

    @Binds
    abstract fun bindUserCollectionsLocalDataSource(
        impl: UserCollectionsLocalDataSourceImpl
    ): UserCollectionsLocalDataSource

    @Binds
    abstract fun bindUserCollectionsRemoteDataSource(
        impl: UserCollectionsRemoteDataSourceImpl
    ): UserCollectionsRemoteDataSource

    @Binds
    abstract fun bindLocalPreferencesDataSource(
        impl: LocalPreferencesDataSourceImpl
    ): LocalPreferencesDataSource

    companion object {

        private const val API_ADDRESS = "https://pro-api.coinmarketcap.com/"
        private const val APPLICATION_JSON = "application/json"

        private val json = Json { ignoreUnknownKeys = true }

        @OptIn(ExperimentalSerializationApi::class)
        @Provides
        @Singleton
        fun provideCoinMarketCapService(): CoinMarketCapService = Retrofit.Builder()
            .baseUrl(API_ADDRESS)
            .addConverterFactory(json.asConverterFactory(MediaType.get(
                APPLICATION_JSON)))
            .build().create(CoinMarketCapService::class.java)

        @Provides
        fun provideFirestoreDatabase(): FirebaseFirestore = Firebase.firestore

        @Provides
        fun provideLocalPreferencesRepository(
            localPreferencesDataSource: LocalPreferencesDataSource,
        ): LocalPreferencesRepository {
            return LocalPreferencesRepository(localPreferencesDataSource)
        }

        @Provides
        fun provideLocalPreferencesDao(
            @ApplicationContext context: Context
        ): LocalPreferencesDao =
            CryptoHubDatabase.getInstance(context).localPreferencesDao()

        @Provides
        fun provideCryptoCollectionsDao(
            @ApplicationContext context: Context
        ): CryptoCollectionsDao =
            CryptoHubDatabase.getInstance(context).cryptoCollectionsDao()

        @Provides
        fun provideUserCollectionsRepository(
            userCollectionsLocalDataSource: UserCollectionsLocalDataSource,
            userCollectionsRemoteDataSource: UserCollectionsRemoteDataSource,
            userDataSource: UserDataSource
        ): UserCollectionsRepository {
            return UserCollectionsRepository(
                userCollectionsRemoteDataSource,
                userCollectionsLocalDataSource,
                userDataSource
            )
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

        @Provides
        fun provideNotifier(
            @ApplicationContext context: Context,
            localPreferencesRepository: LocalPreferencesRepository,
            workManager: WorkManager
        ): Notifier {
            return NotifierImpl(context, localPreferencesRepository, workManager)
        }
    }
}
