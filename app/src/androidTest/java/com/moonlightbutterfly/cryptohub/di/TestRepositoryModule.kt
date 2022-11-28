package com.moonlightbutterfly.cryptohub.di

import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.moonlightbutterfly.cryptohub.data.CryptoAssetsDataSource
import com.moonlightbutterfly.cryptohub.data.CryptoAssetsRepository
import com.moonlightbutterfly.cryptohub.data.LocalPreferencesDataSource
import com.moonlightbutterfly.cryptohub.data.LocalPreferencesRepository
import com.moonlightbutterfly.cryptohub.data.UserCollectionsLocalDataSource
import com.moonlightbutterfly.cryptohub.data.UserCollectionsRemoteDataSource
import com.moonlightbutterfly.cryptohub.data.UserCollectionsRepository
import com.moonlightbutterfly.cryptohub.data.UserDataSource
import com.moonlightbutterfly.cryptohub.data.UserRepository
import com.moonlightbutterfly.cryptohub.framework.data.UserDataSourceImpl
import com.moonlightbutterfly.cryptohub.repository.FakeCryptoAssetsDataSourceImpl
import com.moonlightbutterfly.cryptohub.repository.FakeFirebaseSignInHandler
import com.moonlightbutterfly.cryptohub.repository.FakeLocalPreferencesDataSourceImpl
import com.moonlightbutterfly.cryptohub.repository.FakeUserCollectionsLocalDataSourceImpl
import com.moonlightbutterfly.cryptohub.repository.FakeUserCollectionsRemoteDataSourceImpl
import dagger.Module
import dagger.Provides

@Module
class TestRepositoryModule {

    @Provides
    fun provideCryptoAssetsDataSource(): CryptoAssetsDataSource {
        return FakeCryptoAssetsDataSourceImpl()
    }

    @Provides
    @ActivityScope
    fun provideUserConfigurationLocalDataSource(): UserCollectionsLocalDataSource {
        return FakeUserCollectionsLocalDataSourceImpl()
    }

    @Provides
    @ActivityScope
    fun provideUserConfigurationRemoteDataSource(): UserCollectionsRemoteDataSource {
        return FakeUserCollectionsRemoteDataSourceImpl()
    }

    @Provides
    fun provideUserConfigurationRepository(
        userCollectionsLocalDataSource: UserCollectionsLocalDataSource,
        userCollectionsRemoteDataSource: UserCollectionsRemoteDataSource,
        userDataSource: UserDataSource
    ): UserCollectionsRepository {
        return UserCollectionsRepository(userCollectionsRemoteDataSource, userCollectionsLocalDataSource, userDataSource)
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
    @ActivityScope
    fun provideLocalPreferencesDataSource(): LocalPreferencesDataSource =
        FakeLocalPreferencesDataSourceImpl()

    @Provides
    @ActivityScope
    fun provideUserDataSource(): UserDataSource = UserDataSourceImpl(
        FakeFirebaseSignInHandler(), FirebaseAuth.getInstance()
    )

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
}
