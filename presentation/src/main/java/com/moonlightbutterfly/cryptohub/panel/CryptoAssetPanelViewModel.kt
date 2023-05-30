package com.moonlightbutterfly.cryptohub.panel

import androidx.lifecycle.SavedStateHandle
import com.moonlightbutterfly.cryptohub.core.BaseViewModel
import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.data.common.getOrNull
import com.moonlightbutterfly.cryptohub.data.common.getOrThrow
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import com.moonlightbutterfly.cryptohub.models.NotificationConfiguration
import com.moonlightbutterfly.cryptohub.models.NotificationInterval
import com.moonlightbutterfly.cryptohub.models.NotificationTime
import com.moonlightbutterfly.cryptohub.usecases.AddFavouriteUseCase
import com.moonlightbutterfly.cryptohub.usecases.ConfigureNotificationsUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetFavouritesUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveFavouriteUseCase
import com.moonlightbutterfly.cryptohub.usecases.UpdateLocalPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
@SuppressWarnings("LongParameterList")
class CryptoAssetPanelViewModel @Inject constructor(
    private val getCryptoAssetsMarketInfoUseCase: GetCryptoAssetsMarketInfoUseCase,
    private val getFavouritesUseCase: GetFavouritesUseCase,
    private val addFavouriteUseCase: AddFavouriteUseCase,
    private val removeFavouriteUseCase: RemoveFavouriteUseCase,
    private val getLocalPreferencesUseCase: GetLocalPreferencesUseCase,
    private val updateLocalPreferencesUseCase: UpdateLocalPreferencesUseCase,
    private val configureNotificationsUseCase: ConfigureNotificationsUseCase,
    private val state: SavedStateHandle,
    initialValue: CryptoAssetPanelUIState
) : BaseViewModel<CryptoAssetPanelIntent, CryptoAssetPanelUIState>(initialValue) {

    init {
        acceptIntent(CryptoAssetPanelIntent.GetData)
    }

    private val asset get() = uiState.value.model?.asset

    private fun isCryptoInFavourites(favourites: CryptoCollection, symbol: String) =
        favourites.cryptoAssets.find { it.symbol == symbol } != null

    private fun isCryptoInNotifications(preferences: LocalPreferences, symbol: String) =
        preferences.notificationsConfiguration.find {
            it.symbol == symbol
        } != null

    private fun getConfigurationForCrypto(preferences: LocalPreferences, symbol: String) =
        preferences.notificationsConfiguration.find {
            it.symbol == symbol
        }

    private fun addCryptoToFavourites() = flow {
        asset?.let {
            addFavouriteUseCase(it).getOrNull()?.let {
                emit(
                    uiState.value.copy(isInFavourites = true)
                )
            }
        }
    }

    private fun removeCryptoFromFavourites() = flow {
        asset?.let {
            removeFavouriteUseCase(it).getOrNull()?.let {
                emit(
                    uiState.value.copy(isInFavourites = false)
                )
            }
        }
    }

    private fun addCryptoToNotifications(time: NotificationTime?, interval: NotificationInterval?) =
        flow {
            asset?.let { asset ->
                (getLocalPreferencesUseCase().first() as? Answer.Success)?.let { answer ->
                    val cryptoConfiguration = NotificationConfiguration(asset.symbol, interval, time)
                    val newSet = answer.freshConfiguration() + cryptoConfiguration
                    updateLocalPreferencesUseCase(answer.data.copy(notificationsConfiguration = newSet))
                    configureNotificationsUseCase(newSet)
                    emit(
                        uiState.value.copy(
                            isInNotifications = true,
                            notificationConfiguration = cryptoConfiguration
                        )
                    )
                }
            }
        }

    private fun removeCryptoFromNotifications() = flow {
        asset?.let { asset ->
            (getLocalPreferencesUseCase().first() as? Answer.Success)?.let { answer ->
                val newSet = answer.freshConfiguration()
                updateLocalPreferencesUseCase(answer.data.copy(notificationsConfiguration = newSet))
                configureNotificationsUseCase(newSet)
                emit(
                    uiState.value.copy(
                        isInNotifications = false,
                        notificationConfiguration = NotificationConfiguration(asset.symbol)
                    )
                )
            }
        }
    }

    private fun Answer.Success<LocalPreferences>.freshConfiguration(): Set<NotificationConfiguration> {
        return data.notificationsConfiguration.filter { it.symbol != asset?.symbol }.toSet()
    }

    private fun getNewData(): Flow<CryptoAssetPanelUIState> = flow {
        try {
            val marketInfo = getCryptoAssetsMarketInfoUseCase(listOf(state.get<String>(SAVE_STATE_SYMBOL_KEY)!!))
                .first().getOrThrow().first()
            val favourites = getFavouritesUseCase().first().getOrNull()
            val preferences = getLocalPreferencesUseCase().first().getOrThrow()
            val isInNotifications = isCryptoInNotifications(preferences, marketInfo.asset.symbol)
            val isInFavourites = isCryptoInFavourites(favourites ?: CryptoCollection.EMPTY, marketInfo.asset.symbol)
            val configuration = getConfigurationForCrypto(preferences, marketInfo.asset.symbol)
                ?: NotificationConfiguration(marketInfo.asset.symbol)
            emit(
                CryptoAssetPanelUIState(
                    marketInfo,
                    preferences.notificationsEnabled,
                    isInNotifications,
                    isInFavourites,
                    configuration,
                    isLoading = false
                )
            )
        } catch (exception: java.lang.Exception) {
            emit(CryptoAssetPanelUIState(error = exception))
        }
    }

    override fun mapIntent(intent: CryptoAssetPanelIntent): Flow<CryptoAssetPanelUIState> {
        return when (intent) {
            is CryptoAssetPanelIntent.GetData -> getNewData()
            is CryptoAssetPanelIntent.AddToFavourites -> addCryptoToFavourites()
            is CryptoAssetPanelIntent.RemoveFromFavourites -> removeCryptoFromFavourites()
            is CryptoAssetPanelIntent.ScheduleNotifications -> addCryptoToNotifications(
                intent.configuration.notificationTime,
                intent.configuration.notificationInterval
            )
            is CryptoAssetPanelIntent.ClearNotifications -> removeCryptoFromNotifications()
        }
    }

    private companion object {
        private const val SAVE_STATE_SYMBOL_KEY = "symbol"
    }
}
