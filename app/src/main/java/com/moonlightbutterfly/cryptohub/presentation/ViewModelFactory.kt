package com.moonlightbutterfly.cryptohub.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moonlightbutterfly.cryptohub.presentation.viewmodels.CryptoAssetPanelViewModel
import com.moonlightbutterfly.cryptohub.presentation.viewmodels.CryptoAssetsListViewModel
import com.moonlightbutterfly.cryptohub.presentation.viewmodels.MainViewModel
import com.moonlightbutterfly.cryptohub.presentation.viewmodels.SearchViewModel
import com.moonlightbutterfly.cryptohub.presentation.viewmodels.SettingsViewModel
import com.moonlightbutterfly.cryptohub.presentation.viewmodels.SignInViewModel
import javax.inject.Inject
import javax.inject.Provider

/**
 * Factory for all app's ViewModels. When a ViewModel is provided, it should be provided through
 * this factory.
 */
class ViewModelFactory @Inject constructor(
    private val cryptoAssetsViewModelProvider: Provider<CryptoAssetsListViewModel>,
    private val mainViewModelProvider: Provider<MainViewModel>,
    private val settingsViewModelProvider: Provider<SettingsViewModel>,
    private val cryptoAssetPanelViewModel: Provider<CryptoAssetPanelViewModel>,
    private val searchViewModel: Provider<SearchViewModel>,
    private val signInViewModel: Provider<SignInViewModel>,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.let {
            when {
                it.isAssignableFrom(CryptoAssetsListViewModel::class.java) -> cryptoAssetsViewModelProvider.get()
                it.isAssignableFrom(MainViewModel::class.java) -> mainViewModelProvider.get()
                it.isAssignableFrom(SettingsViewModel::class.java) -> settingsViewModelProvider.get()
                it.isAssignableFrom(CryptoAssetPanelViewModel::class.java) -> cryptoAssetPanelViewModel.get()
                it.isAssignableFrom(SearchViewModel::class.java) -> searchViewModel.get()
                it.isAssignableFrom(SignInViewModel::class.java) -> signInViewModel.get()
                else -> throw IllegalArgumentException("Unknown ViewModel class")
            } as T
        }
    }
}
