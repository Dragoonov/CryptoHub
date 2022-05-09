package com.moonlightbutterfly.cryptohub.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetSignedInUserUseCase
import com.moonlightbutterfly.cryptohub.usecases.SignOutUserUseCase
import com.moonlightbutterfly.cryptohub.usecases.UpdateLocalPreferencesUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val getLocalPreferencesUseCase: GetLocalPreferencesUseCase,
    private val updateLocalPreferencesUseCase: UpdateLocalPreferencesUseCase,
    private val signOutUserUseCase: SignOutUserUseCase,
    private val getSignedInUserUseCase: GetSignedInUserUseCase
) : ViewModel() {

    val isNightModeEnabled = getLocalPreferencesUseCase().map { it.nightModeEnabled }.asLiveData()

    fun onNightModeChanged(nightModeEnabled: Boolean) {
        viewModelScope.launch {
            val preferences = getLocalPreferencesUseCase().first()
            updateLocalPreferencesUseCase(preferences.copy(nightModeEnabled = nightModeEnabled))
        }
    }

    fun isUserSignedIn(): Boolean {
        return getSignedInUserUseCase() != null
    }

    fun onSignedOut() {
        signOutUserUseCase()
    }
}
