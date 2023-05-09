package com.moonlightbutterfly.cryptohub.di

import android.content.Context
import androidx.work.WorkManager
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.moonlightbutterfly.cryptohub.FirebaseAuthDataProvider
import com.moonlightbutterfly.cryptohub.FirebaseAuthDataProviderImpl
import com.moonlightbutterfly.cryptohub.assets.CoinMarketCapService
import com.moonlightbutterfly.cryptohub.assets.CryptoAssetsDataSourceImpl
import com.moonlightbutterfly.cryptohub.collections.UserCollectionsLocalDataSourceImpl
import com.moonlightbutterfly.cryptohub.collections.UserCollectionsRemoteDataSourceImpl
import com.moonlightbutterfly.cryptohub.common.ErrorMapperImpl
import com.moonlightbutterfly.cryptohub.data.assets.CryptoAssetsDataSource
import com.moonlightbutterfly.cryptohub.data.assets.CryptoAssetsRepository
import com.moonlightbutterfly.cryptohub.data.collections.UserCollectionsLocalDataSource
import com.moonlightbutterfly.cryptohub.data.collections.UserCollectionsRemoteDataSource
import com.moonlightbutterfly.cryptohub.data.collections.UserCollectionsRepository
import com.moonlightbutterfly.cryptohub.data.common.ErrorMapper
import com.moonlightbutterfly.cryptohub.data.localpreferences.LocalPreferencesDataSource
import com.moonlightbutterfly.cryptohub.data.localpreferences.LocalPreferencesRepository
import com.moonlightbutterfly.cryptohub.data.notifications.Notifier
import com.moonlightbutterfly.cryptohub.data.user.UserDataSource
import com.moonlightbutterfly.cryptohub.data.user.UserRepository
import com.moonlightbutterfly.cryptohub.database.CryptoHubDatabase
import com.moonlightbutterfly.cryptohub.database.daos.CryptoCollectionsDao
import com.moonlightbutterfly.cryptohub.database.daos.LocalPreferencesDao
import com.moonlightbutterfly.cryptohub.localpreferences.LocalPreferencesDataSourceImpl
import com.moonlightbutterfly.cryptohub.notifications.NotifierImpl
import com.moonlightbutterfly.cryptohub.signin.FirebaseSignInHandler
import com.moonlightbutterfly.cryptohub.signin.FirebaseSignInHandlerImpl
import com.moonlightbutterfly.cryptohub.user.UserDataSourceImpl
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
    internal abstract fun bindFirebaseAuthDataProvider(provider: FirebaseAuthDataProviderImpl): FirebaseAuthDataProvider

    @Binds
    internal abstract fun bindErrorMapper(errorMapperImpl: ErrorMapperImpl): ErrorMapper

    @Binds
    internal abstract fun bindFirebaseSignInHandler(firebaseSignInHandlerImpl: FirebaseSignInHandlerImpl): FirebaseSignInHandler

    @Binds
    internal abstract fun bindUserDataSource(userDataSourceImpl: UserDataSourceImpl): UserDataSource

    @Binds
    internal abstract fun bindsCryptoAssetsDataSource(impl: CryptoAssetsDataSourceImpl): CryptoAssetsDataSource

    @Binds
    internal abstract fun bindUserCollectionsLocalDataSource(
        impl: UserCollectionsLocalDataSourceImpl
    ): UserCollectionsLocalDataSource

    @Binds
    internal abstract fun bindUserCollectionsRemoteDataSource(
        impl: UserCollectionsRemoteDataSourceImpl
    ): UserCollectionsRemoteDataSource

    @Binds
    internal abstract fun bindLocalPreferencesDataSource(
        impl: LocalPreferencesDataSourceImpl
    ): LocalPreferencesDataSource

    companion object {

        private const val API_ADDRESS = "https://pro-api.coinmarketcap.com/"
        private const val APPLICATION_JSON = "application/json"

        private val json = Json { ignoreUnknownKeys = true }

        @OptIn(ExperimentalSerializationApi::class)
        @Provides
        @Singleton
        internal fun provideCoinMarketCapService(): CoinMarketCapService = Retrofit.Builder()
            .baseUrl(API_ADDRESS)
            .addConverterFactory(
                json.asConverterFactory(
                    MediaType.get(
                        APPLICATION_JSON
                    )
                )
            )
            .build().create(CoinMarketCapService::class.java)

        @Provides
        internal fun provideFirestoreDatabase(): FirebaseFirestore = Firebase.firestore

        @Provides
        internal fun provideLocalPreferencesRepository(
            localPreferencesDataSource: LocalPreferencesDataSource,
        ): LocalPreferencesRepository {
            return LocalPreferencesRepository(localPreferencesDataSource)
        }

        @Provides
        internal fun provideLocalPreferencesDao(
            @ApplicationContext context: Context
        ): LocalPreferencesDao =
            CryptoHubDatabase.getInstance(context).localPreferencesDao()

        @Provides
        internal fun provideCryptoCollectionsDao(
            @ApplicationContext context: Context
        ): CryptoCollectionsDao =
            CryptoHubDatabase.getInstance(context).cryptoCollectionsDao()

        @Provides
        internal fun provideUserCollectionsRepository(
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
        internal fun provideCryptoAssetsRepository(
            cryptoAssetsDataSource: CryptoAssetsDataSource,
        ): CryptoAssetsRepository {
            return CryptoAssetsRepository(cryptoAssetsDataSource)
        }

        @Provides
        internal fun provideUserRepository(
            userDataSource: UserDataSource,
        ): UserRepository {
            return UserRepository(userDataSource)
        }

        @Provides
        internal fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

        @Provides
        internal fun provideAuthProviders(): List<AuthUI.IdpConfig> {
            return arrayListOf(
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.FacebookBuilder().build(),
                AuthUI.IdpConfig.TwitterBuilder().build(),
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.PhoneBuilder().build()
            )
        }

        @Provides
        internal fun provideNotifier(
            @ApplicationContext context: Context,
            localPreferencesRepository: LocalPreferencesRepository,
            workManager: WorkManager
        ): Notifier {
            return NotifierImpl(context, localPreferencesRepository, workManager)
        }
    }
}
