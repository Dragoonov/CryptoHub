package com.moonlightbutterfly.cryptohub.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.moonlightbutterfly.cryptohub.usecases.GetUserSettingsUseCase
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MainViewModel @Inject constructor(
    getUserSettingsUseCase: GetUserSettingsUseCase
) : ViewModel() {

    val isNightModeEnabled = getUserSettingsUseCase().map { it.nightModeEnabled }.asLiveData()
}
