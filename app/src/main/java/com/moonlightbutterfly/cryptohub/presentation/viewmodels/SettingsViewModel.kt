package com.moonlightbutterfly.cryptohub.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.moonlightbutterfly.cryptohub.usecases.GetUserSettingsUseCase
import com.moonlightbutterfly.cryptohub.usecases.UpdateUserSettingsUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val getUserSettingsUseCase: GetUserSettingsUseCase,
    private val updateUserSettingsUseCase: UpdateUserSettingsUseCase
) : ViewModel() {

    val isNightModeEnabled = getUserSettingsUseCase().map { it.nightModeEnabled }.asLiveData()

    fun onNightModeChanged(nightModeEnabled: Boolean) {
        viewModelScope.launch {
            val settings = getUserSettingsUseCase().first()
            updateUserSettingsUseCase(settings.copy(nightModeEnabled = nightModeEnabled))
        }
    }
}
