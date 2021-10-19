package com.moonlightbutterfly.cryptohub.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

/**
 * Factory for all app's ViewModels. When a ViewModel is provided, it should be provided through
 * this factory.
 */
class ViewModelFactory @Inject constructor(
    private val cryptoassetsViewModelProvider: Provider<CryptoassetsListViewModel>,
    private val mainViewModelProvider: Provider<MainViewModel>,
    private val settingsViewModelProvider: Provider<SettingsViewModel>,
    private val cryptoassetPanelViewModel: Provider<CryptoassetPanelViewModel>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.let {
            when {
                it.isAssignableFrom(CryptoassetsListViewModel::class.java) -> cryptoassetsViewModelProvider.get()
                it.isAssignableFrom(MainViewModel::class.java) -> mainViewModelProvider.get()
                it.isAssignableFrom(SettingsViewModel::class.java) -> settingsViewModelProvider.get()
                it.isAssignableFrom(CryptoassetPanelViewModel::class.java) -> cryptoassetPanelViewModel.get()
                else -> throw IllegalArgumentException("Unknown ViewModel class")
            } as T
        }
    }
}
