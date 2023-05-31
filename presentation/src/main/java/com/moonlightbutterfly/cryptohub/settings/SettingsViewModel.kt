package com.moonlightbutterfly.cryptohub.settings

import com.moonlightbutterfly.cryptohub.core.BaseViewModel
import com.moonlightbutterfly.cryptohub.data.common.getOrNull
import com.moonlightbutterfly.cryptohub.data.common.getOrThrow
import com.moonlightbutterfly.cryptohub.data.common.unpack
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import com.moonlightbutterfly.cryptohub.usecases.ConfigureNotificationsUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import com.moonlightbutterfly.cryptohub.usecases.IsUserSignedInUseCase
import com.moonlightbutterfly.cryptohub.usecases.SignOutUseCase
import com.moonlightbutterfly.cryptohub.usecases.UpdateLocalPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getLocalPreferencesUseCase: GetLocalPreferencesUseCase,
    private val updateLocalPreferencesUseCase: UpdateLocalPreferencesUseCase,
    private val signOutUserUseCase: SignOutUseCase,
    private val isUserSignedInUseCase: IsUserSignedInUseCase,
    private val configureNotificationsUseCase: ConfigureNotificationsUseCase,
    initialState: SettingsUIState,
) : BaseViewModel<SettingsIntent, SettingsUIState>(initialState) {

    init {
        acceptIntent(SettingsIntent.GetData)
    }

    private fun onNightModeChanged(nightModeEnabled: Boolean): Flow<SettingsUIState> = flow {
        getLocalPreferencesUseCase().first().unpack(LocalPreferences.DEFAULT).let {
            updateLocalPreferencesUseCase(it.copy(nightModeEnabled = nightModeEnabled)).getOrThrow()
            emit(uiState.value.copy(nightModeEnabled = nightModeEnabled))
        }
    }

    private fun onSignedOut(): Flow<SettingsUIState> = flow {
        signOutUserUseCase().getOrNull()?.let {
            emit(uiState.value.copy(isUserSignedIn = false))
        }
    }

    private fun onEnabledNotifications(): Flow<SettingsUIState> = flow {
        getLocalPreferencesUseCase().first().unpack(LocalPreferences.DEFAULT).let {
            configureNotificationsUseCase(it.notificationsConfiguration).getOrThrow()
            updateLocalPreferencesUseCase(it.copy(notificationsEnabled = true)).getOrThrow()
            emit(uiState.value.copy(notificationsEnabled = true))
        }
    }

    private fun onDisabledNotifications(): Flow<SettingsUIState> = flow {
        getLocalPreferencesUseCase().first().unpack(LocalPreferences.DEFAULT).let {
            configureNotificationsUseCase(emptySet()).getOrThrow()
            updateLocalPreferencesUseCase(it.copy(notificationsEnabled = false)).getOrThrow()
            emit(uiState.value.copy(notificationsEnabled = false))
        }
    }

    private fun getData(): Flow<SettingsUIState> = flow {
        val preferences =
            getLocalPreferencesUseCase().map { it.unpack(LocalPreferences.DEFAULT) }.first()
        val isUserSignedIn = isUserSignedInUseCase().unpack(false)
        emit(
            SettingsUIState(
                nightModeEnabled = preferences.nightModeEnabled,
                notificationsEnabled = preferences.notificationsEnabled,
                notificationSymbols = preferences.notificationsConfiguration.map { it.symbol },
                isUserSignedIn = isUserSignedIn,
                isLoading = false,
                error = null
            )
        )
    }

    override fun mapIntent(intent: SettingsIntent): Flow<SettingsUIState> {
        return when (intent) {
            is SettingsIntent.GetData -> getData()
            is SettingsIntent.ChangeNightMode -> onNightModeChanged(intent.enable)
            is SettingsIntent.DisableNotifications -> onDisabledNotifications()
            is SettingsIntent.EnableNotifications -> onEnabledNotifications()
            is SettingsIntent.SignOut -> onSignedOut()
        }
    }
}
