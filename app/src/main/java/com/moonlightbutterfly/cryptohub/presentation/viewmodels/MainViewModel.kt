package com.moonlightbutterfly.cryptohub.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MainViewModel @Inject constructor(
    getLocalPreferencesUseCase: GetLocalPreferencesUseCase
) : ViewModel() {

    val isNightModeEnabled = getLocalPreferencesUseCase().map { it.nightModeEnabled }.asLiveData()
}
