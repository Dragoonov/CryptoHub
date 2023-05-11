package com.moonlightbutterfly.cryptohub

import com.moonlightbutterfly.cryptohub.data.assets.CryptoAssetsDataSource
import com.moonlightbutterfly.cryptohub.data.assets.CryptoAssetsRepository
import com.moonlightbutterfly.cryptohub.data.collections.FakeUserCollectionsLocalDataSourceImpl
import com.moonlightbutterfly.cryptohub.data.collections.FakeUserCollectionsRemoteDataSourceImpl
import com.moonlightbutterfly.cryptohub.data.collections.UserCollectionsLocalDataSource
import com.moonlightbutterfly.cryptohub.data.collections.UserCollectionsRemoteDataSource
import com.moonlightbutterfly.cryptohub.data.collections.UserCollectionsRepository
import com.moonlightbutterfly.cryptohub.data.collections.assets.FakeCryptoAssetsDataSourceImpl
import com.moonlightbutterfly.cryptohub.data.localpreferences.FakeLocalPreferencesDataSourceImpl
import com.moonlightbutterfly.cryptohub.data.localpreferences.LocalPreferencesDataSource
import com.moonlightbutterfly.cryptohub.data.localpreferences.LocalPreferencesRepository
import com.moonlightbutterfly.cryptohub.data.notifications.FakeNotifierImpl
import com.moonlightbutterfly.cryptohub.data.notifications.Notifier
import com.moonlightbutterfly.cryptohub.data.user.FakeUserDataSourceImpl
import com.moonlightbutterfly.cryptohub.data.user.UserDataSource
import com.moonlightbutterfly.cryptohub.data.user.UserRepository
import com.moonlightbutterfly.cryptohub.di.RepositoryModule
import dagger.Binds
import dagger.Module
import dagger.Provides
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
        fun provideNotifier(): Notifier {
            return FakeNotifierImpl()
        }
    }
}
