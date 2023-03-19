package com.moonlightbutterfly.cryptohub.presentation.settings

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.data.common.unpack
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import com.moonlightbutterfly.cryptohub.presentation.core.BaseViewModel
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
) : BaseViewModel() {

    val isNightModeEnabled = getLocalPreferencesUseCase().propagateErrors()
        .map { it.unpack(LocalPreferences.DEFAULT).nightModeEnabled }.asLiveData()

    val areNotificationsEnabled = getLocalPreferencesUseCase().propagateErrors()
        .map { it.unpack(LocalPreferences.DEFAULT).notificationsEnabled }.asLiveData()

    val isUserSignedIn = flow {
        emit(isUserSignedInUseCase().propagateErrors().unpack(false))
    }

    val notificationsSymbols = getLocalPreferencesUseCase().propagateErrors()
        .map {
            it.unpack(LocalPreferences.DEFAULT).notificationsConfiguration
                .map { crypto -> crypto.symbol }
        }

    fun onNightModeChanged(nightModeEnabled: Boolean) {
        viewModelScope.launch {
            getLocalPreferencesUseCase().first()
                .propagateErrors()
                .let {
                    if (it is Result.Success) {
                        updateLocalPreferencesUseCase(it.data.copy(nightModeEnabled = nightModeEnabled))
                            .propagateErrors()
                    }
                }
        }
    }

    fun onSignedOut() {
        viewModelScope.launch {
            signOutUserUseCase().propagateErrors()
        }
    }

    fun onEnabledNotifications() {
        viewModelScope.launch {
            getLocalPreferencesUseCase().first()
                .propagateErrors()
                .let {
                    if (it is Result.Success) {
                        configureNotificationsUseCase(it.data.notificationsConfiguration).propagateErrors()
                        updateLocalPreferencesUseCase(it.data.copy(notificationsEnabled = true)).propagateErrors()
                    }
                }
        }
    }

    fun onDisabledNotifications() {
        viewModelScope.launch {
            getLocalPreferencesUseCase().first()
                .propagateErrors()
                .let {
                    if (it is Result.Success) {
                        configureNotificationsUseCase(emptySet()).propagateErrors()
                        updateLocalPreferencesUseCase(it.data.copy(notificationsEnabled = false)).propagateErrors()
                    }
                }
        }
    }
}
