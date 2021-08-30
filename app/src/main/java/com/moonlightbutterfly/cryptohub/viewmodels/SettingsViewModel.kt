package com.moonlightbutterfly.cryptohub.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonlightbutterfly.cryptohub.usecases.GetAppConfigSettingsUseCase
import com.moonlightbutterfly.cryptohub.usecases.SetAppConfigUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val getAppConfigSettingsUseCase: GetAppConfigSettingsUseCase,
    private val setAppConfigUseCase: SetAppConfigUseCase
) : ViewModel() {

    val isNightModeEnabled = getAppConfigSettingsUseCase.isNightModeEnabled

    fun onNightModeChanged(nightModeEnabled: Boolean) {
        viewModelScope.launch {
            val appConfig = getAppConfigSettingsUseCase.appConfig.value
            appConfig?.let {
                setAppConfigUseCase.updateAppConfig(it.copy(nightModeEnabled = nightModeEnabled))
            }
        }
    }
}
