package com.moonlightbutterfly.cryptohub.di

import com.moonlightbutterfly.cryptohub.data.CryptoAssetsRepository
import com.moonlightbutterfly.cryptohub.data.LocalPreferencesRepository
import com.moonlightbutterfly.cryptohub.data.UserCollectionsRepository
import com.moonlightbutterfly.cryptohub.data.UserDataCache
import com.moonlightbutterfly.cryptohub.usecases.AddAssetToCollectionUseCase
import com.moonlightbutterfly.cryptohub.usecases.AddFavouriteUseCase
import com.moonlightbutterfly.cryptohub.usecases.AddRecentUseCase
import com.moonlightbutterfly.cryptohub.usecases.ClearRecentsUseCase
import com.moonlightbutterfly.cryptohub.usecases.CreateCollectionUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetAllCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetCollectionUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetFavouritesUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetRecentsUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetSignedInUserUseCase
import com.moonlightbutterfly.cryptohub.usecases.IsUserSignedInUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveAssetFromCollectionUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveCollectionUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveFavouriteUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveRecentUseCase
import com.moonlightbutterfly.cryptohub.usecases.SignInUserUseCase
import com.moonlightbutterfly.cryptohub.usecases.SignOutUserUseCase
import com.moonlightbutterfly.cryptohub.usecases.UpdateLocalPreferencesUseCase
import dagger.Module
import dagger.Provides

@Module
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
        userDataCache: UserDataCache
    ): GetSignedInUserUseCase {
        return GetSignedInUserUseCase(userDataCache)
    }

    @Provides
    fun provideSignInUserUseCase(
        userDataCache: UserDataCache
    ): SignInUserUseCase {
        return SignInUserUseCase(userDataCache)
    }

    @Provides
    fun provideSignOutUserUseCase(
        userDataCache: UserDataCache
    ): SignOutUserUseCase {
        return SignOutUserUseCase(userDataCache)
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
        userDataCache: UserDataCache
    ): IsUserSignedInUseCase {
        return IsUserSignedInUseCase(userDataCache)
    }
}
