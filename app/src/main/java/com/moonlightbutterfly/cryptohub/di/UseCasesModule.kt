package com.moonlightbutterfly.cryptohub.di

import com.moonlightbutterfly.cryptohub.data.CryptoAssetsRepository
import com.moonlightbutterfly.cryptohub.data.LocalPreferencesRepository
import com.moonlightbutterfly.cryptohub.data.UserConfigurationRepository
import com.moonlightbutterfly.cryptohub.usecases.AddFavouriteUseCase
import com.moonlightbutterfly.cryptohub.usecases.AddRecentUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetAllCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetFavouritesUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetRecentsUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveFavouriteUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveRecentUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveRecentsUseCase
import com.moonlightbutterfly.cryptohub.usecases.UpdateLocalPreferencesUseCase
import dagger.Module
import dagger.Provides

@Module
class UseCasesModule {

    @Provides
    fun provideAddRecentUseCase(
        userConfigurationRepository: UserConfigurationRepository
    ): AddRecentUseCase {
        return AddRecentUseCase(userConfigurationRepository)
    }

    @Provides
    fun provideGetRecentsUseCase(
        userConfigurationRepository: UserConfigurationRepository
    ): GetRecentsUseCase {
        return GetRecentsUseCase(userConfigurationRepository)
    }

    @Provides
    fun provideRemoveRecentsUseCase(
        userConfigurationRepository: UserConfigurationRepository
    ): RemoveRecentsUseCase {
        return RemoveRecentsUseCase(userConfigurationRepository)
    }

    @Provides
    fun provideRemoveRecentUseCase(
        userConfigurationRepository: UserConfigurationRepository
    ): RemoveRecentUseCase {
        return RemoveRecentUseCase(userConfigurationRepository)
    }

    @Provides
    fun provideAddFavouriteUseCase(
        userConfigurationRepository: UserConfigurationRepository
    ): AddFavouriteUseCase {
        return AddFavouriteUseCase(userConfigurationRepository)
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
        userConfigurationRepository: UserConfigurationRepository
    ): GetFavouritesUseCase {
        return GetFavouritesUseCase(userConfigurationRepository)
    }

    @Provides
    fun provideGetLocalPreferencesUseCase(
        localPreferencesRepository: LocalPreferencesRepository
    ): GetLocalPreferencesUseCase {
        return GetLocalPreferencesUseCase(localPreferencesRepository)
    }

    @Provides
    fun provideRemoveFavouriteUseCase(
        userConfigurationRepository: UserConfigurationRepository
    ): RemoveFavouriteUseCase {
        return RemoveFavouriteUseCase(userConfigurationRepository)
    }

    @Provides
    fun provideUpdateLocalPreferencesUseCase(
        localPreferencesRepository: LocalPreferencesRepository
    ): UpdateLocalPreferencesUseCase {
        return UpdateLocalPreferencesUseCase(localPreferencesRepository)
    }
}
