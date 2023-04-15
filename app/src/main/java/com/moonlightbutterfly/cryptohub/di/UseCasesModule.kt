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
        return AddRecentUseCase {
            addAssetToCollectionUseCase.invoke(
                it,
                UserCollectionsRepository.RECENTS_COLLECTION_NAME
            )
        }
    }

    @Provides
    fun provideGetRecentsUseCase(
        getCollectionUseCase: GetCollectionUseCase
    ): GetRecentsUseCase {
        return GetRecentsUseCase { getCollectionUseCase(UserCollectionsRepository.RECENTS_COLLECTION_NAME) }
    }

    @Provides
    fun provideRemoveRecentsUseCase(
        userCollectionsRepository: UserCollectionsRepository
    ): ClearRecentsUseCase {
        return ClearRecentsUseCase {
            userCollectionsRepository.clearCollection(
                UserCollectionsRepository.RECENTS_COLLECTION_NAME
            )
        }
    }

    @Provides
    fun provideRemoveRecentUseCase(
        removeAssetFromCollectionUseCase: RemoveAssetFromCollectionUseCase
    ): RemoveRecentUseCase {
        return RemoveRecentUseCase { removeAssetFromCollectionUseCase(it, UserCollectionsRepository.RECENTS_COLLECTION_NAME) }
    }

    @Provides
    fun provideAddFavouriteUseCase(
        addAssetToCollectionUseCase: AddAssetToCollectionUseCase
    ): AddFavouriteUseCase {
        return AddFavouriteUseCase {
            addAssetToCollectionUseCase(
                it,
                UserCollectionsRepository.FAVOURITES_COLLECTION_NAME
            )
        }
    }

    @Provides
    fun provideGetAllCryptoAssetsMarketInfoUseCase(
        cryptoAssetsRepository: CryptoAssetsRepository
    ): GetAllCryptoAssetsMarketInfoUseCase {
        return GetAllCryptoAssetsMarketInfoUseCase(cryptoAssetsRepository::getCryptoAssetsMarketInfo)
    }

    @Provides
    fun provideGetCryptoAssetsMarketInfoUseCase(
        cryptoAssetsRepository: CryptoAssetsRepository
    ): GetCryptoAssetsMarketInfoUseCase {
        return GetCryptoAssetsMarketInfoUseCase(cryptoAssetsRepository::getCryptoAssetsMarketInfo)
    }

    @Provides
    fun provideGetFavouritesUseCase(
        getCollectionUseCase: GetCollectionUseCase
    ): GetFavouritesUseCase {
        return GetFavouritesUseCase { getCollectionUseCase(UserCollectionsRepository.FAVOURITES_COLLECTION_NAME) }
    }

    @Provides
    fun provideGetLocalPreferencesUseCase(
        localPreferencesRepository: LocalPreferencesRepository
    ): GetLocalPreferencesUseCase {
        return GetLocalPreferencesUseCase(localPreferencesRepository::getLocalPreferences)
    }

    @Provides
    fun provideRemoveFavouriteUseCase(
        removeAssetFromCollectionUseCase: RemoveAssetFromCollectionUseCase
    ): RemoveFavouriteUseCase {
        return RemoveFavouriteUseCase {
            removeAssetFromCollectionUseCase(
                it,
                UserCollectionsRepository.FAVOURITES_COLLECTION_NAME
            )
        }
    }

    @Provides
    fun provideUpdateLocalPreferencesUseCase(
        localPreferencesRepository: LocalPreferencesRepository
    ): UpdateLocalPreferencesUseCase {
        return UpdateLocalPreferencesUseCase(localPreferencesRepository::updateLocalPreferences)
    }

    @Provides
    fun provideGetSignedInUserUseCase(
        userRepository: UserRepository
    ): GetSignedInUserUseCase {
        return GetSignedInUserUseCase(userRepository::getUser)
    }

    @Provides
    fun provideGoogleSignInUseCase(userRepository: UserRepository): GoogleSignInUseCase {
        return GoogleSignInUseCase(userRepository::googleSignIn)
    }

    @Provides
    fun provideEmailSignInUseCase(userRepository: UserRepository): EmailSignInUseCase {
        return EmailSignInUseCase(userRepository::emailSignIn)
    }

    @Provides
    fun provideFacebookSignInUseCase(userRepository: UserRepository): FacebookSignInUseCase {
        return FacebookSignInUseCase(userRepository::facebookSignIn)
    }

    @Provides
    fun providePhoneSignInUseCase(userRepository: UserRepository): PhoneSignInUseCase {
        return PhoneSignInUseCase(userRepository::phoneSignIn)
    }

    @Provides
    fun provideTwitterSignInUseCase(userRepository: UserRepository): TwitterSignInUseCase {
        return TwitterSignInUseCase(userRepository::twitterSignIn)
    }

    @Provides
    fun provideSignOutUserUseCase(
        userRepository: UserRepository
    ): SignOutUseCase {
        return SignOutUseCase(userRepository::signOut)
    }

    @Provides
    fun provideGetCollectionUseCase(
        userCollectionsRepository: UserCollectionsRepository
    ): GetCollectionUseCase {
        return GetCollectionUseCase(userCollectionsRepository::getCollection)
    }

    @Provides
    fun provideAddAssetToCollectionUseCase(
        userCollectionsRepository: UserCollectionsRepository
    ): AddAssetToCollectionUseCase {
        return AddAssetToCollectionUseCase(userCollectionsRepository::addToCollection)
    }

    @Provides
    fun provideCreateCollectionUseCase(
        userCollectionsRepository: UserCollectionsRepository
    ): CreateCollectionUseCase {
        return CreateCollectionUseCase(userCollectionsRepository::createCollection)
    }

    @Provides
    fun provideRemoveAssetFromCollectionUseCase(
        userCollectionsRepository: UserCollectionsRepository
    ): RemoveAssetFromCollectionUseCase {
        return RemoveAssetFromCollectionUseCase(userCollectionsRepository::removeFromCollection)
    }

    @Provides
    fun provideRemoveCollectionUseCase(
        userCollectionsRepository: UserCollectionsRepository
    ): RemoveCollectionUseCase {
        return RemoveCollectionUseCase(userCollectionsRepository::removeCollection)
    }

    @Provides
    fun provideIsUserSignedInUseCase(
        userRepository: UserRepository
    ): IsUserSignedInUseCase {
        return IsUserSignedInUseCase(userRepository::isUserSignedIn)
    }

    @Provides
    fun provideConfigureNotificationsUseCase(
        notifier: Notifier
    ): ConfigureNotificationsUseCase {
        return ConfigureNotificationsUseCase(notifier::configure)
    }
}
