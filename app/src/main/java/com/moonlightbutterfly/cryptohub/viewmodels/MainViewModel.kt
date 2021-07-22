package com.moonlightbutterfly.cryptohub.viewmodels

import androidx.lifecycle.ViewModel
import com.moonlightbutterfly.cryptohub.usecases.GetAppConfigSettingsUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    getAppConfigSettingsUseCase: GetAppConfigSettingsUseCase,
) : ViewModel() {

    val isNightModeEnabled = getAppConfigSettingsUseCase.isNightModeEnabled

}