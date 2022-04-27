package com.moonlightbutterfly.cryptohub.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.moonlightbutterfly.cryptohub.domain.models.UserData
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import com.moonlightbutterfly.cryptohub.usecases.UpdateLocalPreferencesUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val getLocalPreferencesUseCase: GetLocalPreferencesUseCase,
    private val updateLocalPreferencesUseCase: UpdateLocalPreferencesUseCase,
    private val userData: UserData
) : ViewModel() {

    val isNightModeEnabled = getLocalPreferencesUseCase().map { it.nightModeEnabled }.asLiveData()

    fun onNightModeChanged(nightModeEnabled: Boolean) {
        viewModelScope.launch {
            val preferences = getLocalPreferencesUseCase().first()
            updateLocalPreferencesUseCase(preferences.copy(nightModeEnabled = nightModeEnabled))
        }
    }

    fun isUserSignedIn(): Boolean {
        return userData.isUserSignedIn()
    }

    fun onSignedOut() {
        userData.clear()
    }
}
