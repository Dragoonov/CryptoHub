package com.moonlightbutterfly.cryptohub.di

import android.content.Context
import com.facebook.login.LoginManager
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
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
import com.moonlightbutterfly.cryptohub.data.UserDataSource
import com.moonlightbutterfly.cryptohub.data.UserRepository
import com.moonlightbutterfly.cryptohub.framework.CoinMarketCapService
import com.moonlightbutterfly.cryptohub.framework.FirebaseAuthDataProvider
import com.moonlightbutterfly.cryptohub.framework.data.CryptoAssetsDataSourceImpl
import com.moonlightbutterfly.cryptohub.framework.data.ErrorMapperImpl
import com.moonlightbutterfly.cryptohub.framework.data.LocalPreferencesDataSourceImpl
import com.moonlightbutterfly.cryptohub.framework.data.UserCollectionsLocalDataSourceImpl
import com.moonlightbutterfly.cryptohub.framework.data.UserCollectionsRemoteDataSourceImpl
import com.moonlightbutterfly.cryptohub.framework.data.UserDataSourceImpl
import com.moonlightbutterfly.cryptohub.framework.data.signin.FirebaseSignInHandler
import com.moonlightbutterfly.cryptohub.framework.data.signin.FirebaseSignInHandlerImpl
import com.moonlightbutterfly.cryptohub.framework.database.CryptoHubDatabase
import com.moonlightbutterfly.cryptohub.framework.database.daos.CryptoCollectionsDao
import com.moonlightbutterfly.cryptohub.framework.database.daos.LocalPreferencesDao
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
        fun provideUserCollectionsDataSource(
            cryptoCollectionsDao: CryptoCollectionsDao,
            firebaseFirestore: FirebaseFirestore,
            getSignedInUserUseCase: GetSignedInUserUseCase,
            errorMapper: ErrorMapper,
        ): UserCollectionsDataSource {
            val userSignedIn = getSignedInUserUseCase()
            return if (userSignedIn is Result.Success) {
                UserCollectionsRemoteDataSourceImpl(firebaseFirestore,
                    userSignedIn.data.userId,
                    errorMapper)
            } else {
                UserCollectionsLocalDataSourceImpl(cryptoCollectionsDao, errorMapper)
            }
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
            userCollectionsDataSource: UserCollectionsDataSource,
        ): UserCollectionsRepository {
            return UserCollectionsRepository(userCollectionsDataSource)
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
