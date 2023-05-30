package com.moonlightbutterfly.cryptohub.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.data.common.unpack
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import com.moonlightbutterfly.cryptohub.usecases.ConfigureNotificationsUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import com.moonlightbutterfly.cryptohub.usecases.IsUserSignedInUseCase
import com.moonlightbutterfly.cryptohub.usecases.SignOutUseCase
import com.moonlightbutterfly.cryptohub.usecases.UpdateLocalPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getLocalPreferencesUseCase: GetLocalPreferencesUseCase,
    private val updateLocalPreferencesUseCase: UpdateLocalPreferencesUseCase,
    private val signOutUserUseCase: SignOutUseCase,
    private val isUserSignedInUseCase: IsUserSignedInUseCase,
    private val configureNotificationsUseCase: ConfigureNotificationsUseCase,
) : ViewModel() {

    val isNightModeEnabled = getLocalPreferencesUseCase()
        .map { it.unpack(LocalPreferences.DEFAULT).nightModeEnabled }

    val areNotificationsEnabled = getLocalPreferencesUseCase()
        .map { it.unpack(LocalPreferences.DEFAULT).notificationsEnabled }

    val isUserSignedIn = flow {
        emit(isUserSignedInUseCase().unpack(false))
    }

    val notificationsSymbols = getLocalPreferencesUseCase()
        .map {
            it.unpack(LocalPreferences.DEFAULT).notificationsConfiguration
                .map { crypto -> crypto.symbol }
        }

    fun onNightModeChanged(nightModeEnabled: Boolean) {
        viewModelScope.launch {
            getLocalPreferencesUseCase().first()
                .let {
                    if (it is Answer.Success) {
                        updateLocalPreferencesUseCase(it.data.copy(nightModeEnabled = nightModeEnabled))
                    }
                }
        }
    }

    fun onSignedOut() {
        viewModelScope.launch {
            signOutUserUseCase()
        }
    }

    fun onEnabledNotifications() {
        viewModelScope.launch {
            getLocalPreferencesUseCase().first()
                .let {
                    if (it is Answer.Success) {
                        configureNotificationsUseCase(it.data.notificationsConfiguration)
                        updateLocalPreferencesUseCase(it.data.copy(notificationsEnabled = true))
                    }
                }
        }
    }

    fun onDisabledNotifications() {
        viewModelScope.launch {
            getLocalPreferencesUseCase().first()
                .let {
                    if (it is Answer.Success) {
                        configureNotificationsUseCase(emptySet())
                        updateLocalPreferencesUseCase(it.data.copy(notificationsEnabled = false))
                    }
                }
        }
    }
}
