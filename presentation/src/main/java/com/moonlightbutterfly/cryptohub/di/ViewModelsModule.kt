package com.moonlightbutterfly.cryptohub.di

import com.moonlightbutterfly.cryptohub.panel.CryptoAssetPanelUIState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelsModule {

    @Provides
    fun provideCryptoPanelUIInitialState(): CryptoAssetPanelUIState = CryptoAssetPanelUIState(isLoading = true)
}
