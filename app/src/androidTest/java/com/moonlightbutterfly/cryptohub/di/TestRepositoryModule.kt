package com.moonlightbutterfly.cryptohub.di

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
import com.moonlightbutterfly.cryptohub.data.signin.FakeFirebaseSignInHandler
import com.moonlightbutterfly.cryptohub.data.user.UserDataSource
import com.moonlightbutterfly.cryptohub.data.user.UserDataSourceImpl
import com.moonlightbutterfly.cryptohub.data.user.UserRepository
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
