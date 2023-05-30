package com.moonlightbutterfly.cryptohub.core

import androidx.lifecycle.ViewModel
import com.moonlightbutterfly.cryptohub.data.common.unpack
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getLocalPreferencesUseCase: GetLocalPreferencesUseCase
) : ViewModel() {

    val isNightModeEnabled = getLocalPreferencesUseCase()
        .map { it.unpack(LocalPreferences.DEFAULT).nightModeEnabled }
}
