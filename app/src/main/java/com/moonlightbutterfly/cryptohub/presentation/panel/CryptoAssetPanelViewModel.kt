package com.moonlightbutterfly.cryptohub.presentation.panel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.moonlightbutterfly.cryptohub.data.common.Answer
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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@SuppressWarnings("LongParameterList")
class CryptoAssetPanelViewModel @Inject constructor(
    getCryptoAssetsMarketInfoUseCase: GetCryptoAssetsMarketInfoUseCase,
    getFavouritesUseCase: GetFavouritesUseCase,
    private val addFavouriteUseCase: AddFavouriteUseCase,
    private val removeFavouriteUseCase: RemoveFavouriteUseCase,
    private val getLocalPreferencesUseCase: GetLocalPreferencesUseCase,
    private val updateLocalPreferencesUseCase: UpdateLocalPreferencesUseCase,
    private val configureNotificationsUseCase: ConfigureNotificationsUseCase,
    state: SavedStateHandle
) : BaseViewModel() {

    val asset = getCryptoAssetsMarketInfoUseCase(listOf(state.get<String>(SAVE_STATE_SYMBOL_KEY)!!))
        .prepareFlow(emptyList())
        .map {
            it
                .unpack(listOf(CryptoAssetMarketInfo.EMPTY))
                .getOrElse(0) { CryptoAssetMarketInfo.EMPTY }
        }

    private val favourites = getFavouritesUseCase()
        .prepareFlow(CryptoCollection.EMPTY)
        .map { it.unpack(CryptoCollection.EMPTY) }

    fun areNotificationsEnabled() =
        getLocalPreferencesUseCase().map { it.unpack(LocalPreferences.DEFAULT).notificationsEnabled }

    fun isCryptoInFavourites() = favourites.combine(asset) { collection, asset ->
        collection.cryptoAssets.find { it.symbol == asset.asset.symbol } != null
    }

    fun isCryptoInNotifications() =
        getLocalPreferencesUseCase().combine(asset) { preferences, asset ->
            preferences.unpack(LocalPreferences.DEFAULT).notificationsConfiguration.find {
                it.symbol == asset.asset.symbol
            } != null
        }

    fun getConfigurationForCrypto() =
        getLocalPreferencesUseCase().combine(asset) { preferences, asset ->
            preferences.unpack(LocalPreferences.DEFAULT).notificationsConfiguration.find {
                it.symbol == asset.asset.symbol
            }
        }.filterNotNull()

    fun addCryptoToFavourites() {
        viewModelScope.launch {
            asset.firstOrNull()?.let {
                addFavouriteUseCase(it.asset).propagateErrors()
            }
        }
    }

    fun removeCryptoFromFavourites() {
        viewModelScope.launch {
            asset.firstOrNull()?.let {
                removeFavouriteUseCase(it.asset).propagateErrors()
            }
        }
    }

    fun addCryptoToNotifications(
        notificationTime: NotificationTime?,
        notificationInterval: NotificationInterval?
    ) {
        viewModelScope.launch {
            getLocalPreferencesUseCase().first()
                .propagateErrors()
                .let {
                    if (it is Answer.Success) {
                        val newSet = it.data.notificationsConfiguration.filter { crypto ->
                            crypto.symbol != asset.firstOrNull()?.asset?.symbol
                        }.toSet() + NotificationConfiguration(
                            asset.firstOrNull()!!.asset.symbol,
                            notificationInterval,
                            notificationTime
                        )
                        updateLocalPreferencesUseCase(
                            it.data.copy(notificationsConfiguration = newSet)
                        )
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
                    if (it is Answer.Success) {
                        val newSet = it.data.notificationsConfiguration.filter { notification ->
                            notification.symbol != asset.firstOrNull()?.asset?.symbol
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

    private companion object {
        private const val SAVE_STATE_SYMBOL_KEY = "symbol"
    }
}
