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
    private val cryptocurrenciesViewModelProvider: Provider<CryptocurrenciesListViewModel>,
    private val mainViewModelProvider: Provider<MainViewModel>,
    private val settingsViewModelProvider: Provider<SettingsViewModel>,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.let {
            when {
                it.isAssignableFrom(CryptocurrenciesListViewModel::class.java) -> cryptocurrenciesViewModelProvider.get()
                it.isAssignableFrom(MainViewModel::class.java) -> mainViewModelProvider.get()
                it.isAssignableFrom(SettingsViewModel::class.java) -> settingsViewModelProvider.get()
                else -> throw IllegalArgumentException("Unknown ViewModel class")
            } as T
        }
    }
}
