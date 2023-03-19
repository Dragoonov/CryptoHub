package com.moonlightbutterfly.cryptohub.di

import android.content.Context
import androidx.work.WorkManager
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.moonlightbutterfly.cryptohub.data.assets.CryptoAssetsDataSource
import com.moonlightbutterfly.cryptohub.data.assets.CryptoAssetsRepository
import com.moonlightbutterfly.cryptohub.data.assets.FakeCryptoAssetsDataSourceImpl
import com.moonlightbutterfly.cryptohub.data.collections.FakeUserCollectionsLocalDataSourceImpl
import com.moonlightbutterfly.cryptohub.data.collections.FakeUserCollectionsRemoteDataSourceImpl
import com.moonlightbutterfly.cryptohub.data.collections.UserCollectionsLocalDataSource
import com.moonlightbutterfly.cryptohub.data.collections.UserCollectionsRemoteDataSource
import com.moonlightbutterfly.cryptohub.data.collections.UserCollectionsRepository
import com.moonlightbutterfly.cryptohub.data.localpreferences.FakeLocalPreferencesDataSourceImpl
import com.moonlightbutterfly.cryptohub.data.localpreferences.LocalPreferencesDataSource
import com.moonlightbutterfly.cryptohub.data.localpreferences.LocalPreferencesRepository
import com.moonlightbutterfly.cryptohub.data.notifications.Notifier
import com.moonlightbutterfly.cryptohub.data.notifications.NotifierImpl
import com.moonlightbutterfly.cryptohub.data.signin.FakeFirebaseSignInHandler
import com.moonlightbutterfly.cryptohub.data.signin.FirebaseSignInHandler
import com.moonlightbutterfly.cryptohub.data.user.FakeUserDataSourceImpl
import com.moonlightbutterfly.cryptohub.data.user.FirebaseAuthDataProvider
import com.moonlightbutterfly.cryptohub.data.user.FirebaseAuthDataProviderImpl
import com.moonlightbutterfly.cryptohub.data.user.UserDataSource
import com.moonlightbutterfly.cryptohub.data.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [RepositoryModule::class])
abstract class TestRepositoryModule {

    @Binds
    abstract fun bindFirebaseAuthDataProvider(provider: FirebaseAuthDataProviderImpl): FirebaseAuthDataProvider

    @Binds
    abstract fun bindCryptoAssetsDataSource(impl: FakeCryptoAssetsDataSourceImpl): CryptoAssetsDataSource

    @Binds
    abstract fun provideUserConfigurationLocalDataSource(impl: FakeUserCollectionsLocalDataSourceImpl): UserCollectionsLocalDataSource

    @Binds
    abstract fun provideUserConfigurationRemoteDataSource(impl: FakeUserCollectionsRemoteDataSourceImpl): UserCollectionsRemoteDataSource

    @Binds
    abstract fun bindFirebaseSignInHandler(firebaseSignInHandlerImpl: FakeFirebaseSignInHandler): FirebaseSignInHandler

    @Binds
    abstract fun provideLocalPreferencesDataSource(impl: FakeLocalPreferencesDataSourceImpl): LocalPreferencesDataSource

    @Binds
    abstract fun bindUserDataSource(userDataSourceImpl: FakeUserDataSourceImpl): UserDataSource

    companion object {
        @Provides
        fun provideUserConfigurationRepository(
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
        fun provideLocalPreferencesRepository(
            localPreferencesDataSource: LocalPreferencesDataSource
        ): LocalPreferencesRepository {
            return LocalPreferencesRepository(localPreferencesDataSource)
        }

        @Provides
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

        @Provides
        fun provideCryptoAssetsRepository(
            cryptoAssetsDataSource: CryptoAssetsDataSource
        ): CryptoAssetsRepository {
            return CryptoAssetsRepository(cryptoAssetsDataSource)
        }

        @Provides
        fun provideUserRepository(
            userDataSource: UserDataSource
        ): UserRepository {
            return UserRepository(userDataSource)
        }

        @Provides
        fun provideAuthProviders(): List<AuthUI.IdpConfig> {
            return listOf()
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
