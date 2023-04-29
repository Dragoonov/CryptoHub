package com.moonlightbutterfly.cryptohub.core

import com.moonlightbutterfly.cryptohub.data.common.unpack
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getLocalPreferencesUseCase: GetLocalPreferencesUseCase
) : BaseViewModel() {

    val isNightModeEnabled = getLocalPreferencesUseCase()
        .prepareFlow(LocalPreferences.DEFAULT)
        .map { it.unpack(LocalPreferences.DEFAULT).nightModeEnabled }
}
