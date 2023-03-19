package com.moonlightbutterfly.cryptohub.di

import com.moonlightbutterfly.cryptohub.data.assets.CryptoAssetsRepository
import com.moonlightbutterfly.cryptohub.data.collections.UserCollectionsRepository
import com.moonlightbutterfly.cryptohub.data.localpreferences.LocalPreferencesRepository
import com.moonlightbutterfly.cryptohub.data.notifications.Notifier
import com.moonlightbutterfly.cryptohub.data.user.UserRepository
import com.moonlightbutterfly.cryptohub.usecases.AddAssetToCollectionUseCase
import com.moonlightbutterfly.cryptohub.usecases.AddFavouriteUseCase
import com.moonlightbutterfly.cryptohub.usecases.AddRecentUseCase
import com.moonlightbutterfly.cryptohub.usecases.ClearRecentsUseCase
import com.moonlightbutterfly.cryptohub.usecases.ConfigureNotificationsUseCase
import com.moonlightbutterfly.cryptohub.usecases.CreateCollectionUseCase
import com.moonlightbutterfly.cryptohub.usecases.EmailSignInUseCase
import com.moonlightbutterfly.cryptohub.usecases.FacebookSignInUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetAllCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetCollectionUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetFavouritesUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetRecentsUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetSignedInUserUseCase
import com.moonlightbutterfly.cryptohub.usecases.GoogleSignInUseCase
import com.moonlightbutterfly.cryptohub.usecases.IsUserSignedInUseCase
import com.moonlightbutterfly.cryptohub.usecases.PhoneSignInUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveAssetFromCollectionUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveCollectionUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveFavouriteUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveRecentUseCase
import com.moonlightbutterfly.cryptohub.usecases.SignOutUseCase
import com.moonlightbutterfly.cryptohub.usecases.TwitterSignInUseCase
import com.moonlightbutterfly.cryptohub.usecases.UpdateLocalPreferencesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UseCasesModule {

    @Provides
    fun provideAddRecentUseCase(
        addAssetToCollectionUseCase: AddAssetToCollectionUseCase
    ): AddRecentUseCase {
        return AddRecentUseCase(addAssetToCollectionUseCase)
    }

    @Provides
    fun provideGetRecentsUseCase(
        getCollectionUseCase: GetCollectionUseCase
    ): GetRecentsUseCase {
        return GetRecentsUseCase(getCollectionUseCase)
    }

    @Provides
    fun provideRemoveRecentsUseCase(
        userCollectionsRepository: UserCollectionsRepository
    ): ClearRecentsUseCase {
        return ClearRecentsUseCase(userCollectionsRepository)
    }

    @Provides
    fun provideRemoveRecentUseCase(
        removeAssetFromCollectionUseCase: RemoveAssetFromCollectionUseCase
    ): RemoveRecentUseCase {
        return RemoveRecentUseCase(removeAssetFromCollectionUseCase)
    }

    @Provides
    fun provideAddFavouriteUseCase(
        addAssetToCollectionUseCase: AddAssetToCollectionUseCase
    ): AddFavouriteUseCase {
        return AddFavouriteUseCase(addAssetToCollectionUseCase)
    }

    @Provides
    fun provideGetAllCryptoAssetsMarketInfoUseCase(
        cryptoAssetsRepository: CryptoAssetsRepository
    ): GetAllCryptoAssetsMarketInfoUseCase {
        return GetAllCryptoAssetsMarketInfoUseCase(cryptoAssetsRepository)
    }

    @Provides
    fun provideGetCryptoAssetsMarketInfoUseCase(
        cryptoAssetsRepository: CryptoAssetsRepository
    ): GetCryptoAssetsMarketInfoUseCase {
        return GetCryptoAssetsMarketInfoUseCase(cryptoAssetsRepository)
    }

    @Provides
    fun provideGetFavouritesUseCase(
        getCollectionUseCase: GetCollectionUseCase
    ): GetFavouritesUseCase {
        return GetFavouritesUseCase(getCollectionUseCase)
    }

    @Provides
    fun provideGetLocalPreferencesUseCase(
        localPreferencesRepository: LocalPreferencesRepository
    ): GetLocalPreferencesUseCase {
        return GetLocalPreferencesUseCase(localPreferencesRepository)
    }

    @Provides
    fun provideRemoveFavouriteUseCase(
        removeAssetFromCollectionUseCase: RemoveAssetFromCollectionUseCase
    ): RemoveFavouriteUseCase {
        return RemoveFavouriteUseCase(removeAssetFromCollectionUseCase)
    }

    @Provides
    fun provideUpdateLocalPreferencesUseCase(
        localPreferencesRepository: LocalPreferencesRepository
    ): UpdateLocalPreferencesUseCase {
        return UpdateLocalPreferencesUseCase(localPreferencesRepository)
    }

    @Provides
    fun provideGetSignedInUserUseCase(
        userRepository: UserRepository
    ): GetSignedInUserUseCase {
        return GetSignedInUserUseCase(userRepository)
    }

    @Provides
    fun provideGoogleSignInUseCase(userRepository: UserRepository): GoogleSignInUseCase {
        return GoogleSignInUseCase(userRepository)
    }

    @Provides
    fun provideEmailSignInUseCase(userRepository: UserRepository): EmailSignInUseCase {
        return EmailSignInUseCase(userRepository)
    }

    @Provides
    fun provideFacebookSignInUseCase(userRepository: UserRepository): FacebookSignInUseCase {
        return FacebookSignInUseCase(userRepository)
    }

    @Provides
    fun providePhoneSignInUseCase(userRepository: UserRepository): PhoneSignInUseCase {
        return PhoneSignInUseCase(userRepository)
    }

    @Provides
    fun provideTwitterSignInUseCase(userRepository: UserRepository): TwitterSignInUseCase {
        return TwitterSignInUseCase(userRepository)
    }

    @Provides
    fun provideSignOutUserUseCase(
        userRepository: UserRepository
    ): SignOutUseCase {
        return SignOutUseCase(userRepository)
    }

    @Provides
    fun provideGetCollectionUseCase(
        userCollectionsRepository: UserCollectionsRepository
    ): GetCollectionUseCase {
        return GetCollectionUseCase(userCollectionsRepository)
    }

    @Provides
    fun provideAddAssetToCollectionUseCase(
        userCollectionsRepository: UserCollectionsRepository
    ): AddAssetToCollectionUseCase {
        return AddAssetToCollectionUseCase(userCollectionsRepository)
    }

    @Provides
    fun provideCreateCollectionUseCase(
        userCollectionsRepository: UserCollectionsRepository
    ): CreateCollectionUseCase {
        return CreateCollectionUseCase(userCollectionsRepository)
    }

    @Provides
    fun provideRemoveAssetFromCollectionUseCase(
        userCollectionsRepository: UserCollectionsRepository
    ): RemoveAssetFromCollectionUseCase {
        return RemoveAssetFromCollectionUseCase(userCollectionsRepository)
    }

    @Provides
    fun provideRemoveCollectionUseCase(
        userCollectionsRepository: UserCollectionsRepository
    ): RemoveCollectionUseCase {
        return RemoveCollectionUseCase(userCollectionsRepository)
    }

    @Provides
    fun provideIsUserSignedInUseCase(
        userRepository: UserRepository
    ): IsUserSignedInUseCase {
        return IsUserSignedInUseCase(userRepository)
    }

    @Provides
    fun provideConfigureNotificationsUseCase(
        notifier: Notifier
    ): ConfigureNotificationsUseCase {
        return ConfigureNotificationsUseCase(notifier)
    }
}
