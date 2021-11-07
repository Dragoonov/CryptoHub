package com.moonlightbutterfly.cryptohub.di

import com.moonlightbutterfly.cryptohub.data.CryptoAssetsRepository
import com.moonlightbutterfly.cryptohub.data.UserConfigurationRepository
import com.moonlightbutterfly.cryptohub.usecases.AddFavouriteUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetAllCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetFavouritesUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetUserSettingsUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveFavouriteUseCase
import com.moonlightbutterfly.cryptohub.usecases.UpdateUserSettingsUseCase
import dagger.Module
import dagger.Provides

@Module
class UseCasesModule {

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
    fun provideGetUserSettingsUseCase(
        userConfigurationRepository: UserConfigurationRepository
    ): GetUserSettingsUseCase {
        return GetUserSettingsUseCase(userConfigurationRepository)
    }

    @Provides
    fun provideRemoveFavouriteUseCase(
        userConfigurationRepository: UserConfigurationRepository
    ): RemoveFavouriteUseCase {
        return RemoveFavouriteUseCase(userConfigurationRepository)
    }

    @Provides
    fun provideUpdateUserSettingsUseCase(
        userConfigurationRepository: UserConfigurationRepository
    ): UpdateUserSettingsUseCase {
        return UpdateUserSettingsUseCase(userConfigurationRepository)
    }
}
