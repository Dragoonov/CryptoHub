package com.moonlightbutterfly.cryptohub.presentation.core

import com.moonlightbutterfly.cryptohub.data.common.unpack
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MainViewModel @Inject constructor(
    getLocalPreferencesUseCase: GetLocalPreferencesUseCase
) : BaseViewModel() {

    val isNightModeEnabled = getLocalPreferencesUseCase()
        .propagateErrors()
        .map { it.unpack(LocalPreferences.DEFAULT).nightModeEnabled }
}
