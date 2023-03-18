package com.moonlightbutterfly.cryptohub.presentation.panel

import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.data.common.unpack
import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import com.moonlightbutterfly.cryptohub.models.NotificationConfiguration
import com.moonlightbutterfly.cryptohub.models.NotificationInterval
import com.moonlightbutterfly.cryptohub.models.NotificationTime
import com.moonlightbutterfly.cryptohub.presentation.core.BaseViewModel
import com.moonlightbutterfly.cryptohub.usecases.AddFavouriteUseCase
import com.moonlightbutterfly.cryptohub.usecases.ConfigureNotificationsUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetFavouritesUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveFavouriteUseCase
import com.moonlightbutterfly.cryptohub.usecases.UpdateLocalPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoAssetPanelViewModel @Inject constructor(
    private val getCryptoAssetsMarketInfoUseCase: GetCryptoAssetsMarketInfoUseCase,
    getFavouritesUseCase: GetFavouritesUseCase,
    private val addFavouriteUseCase: AddFavouriteUseCase,
    private val removeFavouriteUseCase: RemoveFavouriteUseCase,
    private val getLocalPreferencesUseCase: GetLocalPreferencesUseCase,
    private val updateLocalPreferencesUseCase: UpdateLocalPreferencesUseCase,
    private val configureNotificationsUseCase: ConfigureNotificationsUseCase,
) : BaseViewModel() {

    private lateinit var asset: LiveData<CryptoAssetMarketInfo>

    fun getCryptoAssetMarketInfo(symbol: String): LiveData<CryptoAssetMarketInfo> {
        if (!this::asset.isInitialized) {
            asset = getCryptoAssetsMarketInfoUseCase(listOf(symbol))
                .propagateErrors()
                .map {
                    it
                        .unpack(listOf(CryptoAssetMarketInfo.EMPTY))
                        .getOrElse(0) { CryptoAssetMarketInfo.EMPTY }
                }
                .asLiveData()
        }
        return asset
    }

    private val favourites = getFavouritesUseCase()
        .propagateErrors()
        .map { it.unpack(CryptoCollection.EMPTY) }

    fun areNotificationsEnabled() = getLocalPreferencesUseCase().map { it.unpack(LocalPreferences.DEFAULT).notificationsEnabled }
    fun isCryptoInFavourites() = favourites.combine(asset.asFlow()) { collection, asset ->
        collection.cryptoAssets.find { it.symbol == asset.asset.symbol } != null
    }

    fun isCryptoInNotifications() = getLocalPreferencesUseCase().combine(asset.asFlow()) { preferences, asset ->
        preferences.unpack(LocalPreferences.DEFAULT).notificationsConfiguration.find { it.symbol == asset.asset.symbol } != null
    }

    fun getConfigurationForCrypto() = getLocalPreferencesUseCase().combine(asset.asFlow()) { preferences, asset ->
        preferences.unpack(LocalPreferences.DEFAULT).notificationsConfiguration.find { it.symbol == asset.asset.symbol }
    }.filterNotNull()

    fun addCryptoToFavourites() {
        asset.value?.let {
            viewModelScope.launch {
                addFavouriteUseCase(it.asset).propagateErrors()
            }
        }
    }

    fun removeCryptoFromFavourites() {
        asset.value?.let {
            viewModelScope.launch {
                removeFavouriteUseCase(it.asset).propagateErrors()
            }
        }
    }

    fun addCryptoToNotifications(notificationTime: NotificationTime?, notificationInterval: NotificationInterval?) {
        viewModelScope.launch {
            getLocalPreferencesUseCase().first()
                .propagateErrors()
                .let {
                    if (it is Result.Success) {
                        val newSet = it.data.notificationsConfiguration.filter { crypto -> crypto.symbol != asset.value?.asset?.symbol }.toSet() + NotificationConfiguration(asset.value!!.asset.symbol, notificationInterval, notificationTime)
                        updateLocalPreferencesUseCase(it.data.copy(notificationsConfiguration = newSet))
                            .propagateErrors()
                        configureNotificationsUseCase(newSet).propagateErrors()
                    }
                }
        }
    }

    fun removeCryptoFromNotifications() {
        viewModelScope.launch {
            getLocalPreferencesUseCase().first()
                .propagateErrors()
                .let {
                    if (it is Result.Success) {
                        val newSet = it.data.notificationsConfiguration.filter { notification ->
                            notification.symbol != asset.value?.asset?.symbol
                        }.toSet()
                        updateLocalPreferencesUseCase(
                            it.data.copy(
                                notificationsConfiguration = newSet
                            )
                        ).propagateErrors()
                        configureNotificationsUseCase(newSet).propagateErrors()
                    }
                }
        }
    }
}
