package com.moonlightbutterfly.cryptohub.di

import com.moonlightbutterfly.cryptohub.data.common.unpack
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import com.moonlightbutterfly.cryptohub.panel.CryptoAssetPanelUIState
import com.moonlightbutterfly.cryptohub.settings.SettingsUIState
import com.moonlightbutterfly.cryptohub.signin.SignInUIState
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import com.moonlightbutterfly.cryptohub.usecases.IsUserSignedInUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelsModule {

    @Provides
    fun provideCryptoPanelUIInitialState(): CryptoAssetPanelUIState =
        CryptoAssetPanelUIState(isLoading = true)

    @Provides
    fun provideSettingsInitialState(): SettingsUIState = SettingsUIState(isLoading = true)

    @Provides
    fun provideSignInInitialState(
        isUserSignedInUseCase: IsUserSignedInUseCase,
        getLocalPreferencesUseCase: GetLocalPreferencesUseCase
    ): SignInUIState = SignInUIState(
        nightModeEnabled = runBlocking {
            getLocalPreferencesUseCase().first().unpack(
                LocalPreferences.DEFAULT
            )
        }.nightModeEnabled,
        isUserSignedIn = runBlocking { isUserSignedInUseCase().unpack(false) },
        isLoading = false,
        error = null
    )
}
