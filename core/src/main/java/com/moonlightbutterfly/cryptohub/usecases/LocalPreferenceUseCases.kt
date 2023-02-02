package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.localpreferences.LocalPreferencesRepository
import com.moonlightbutterfly.cryptohub.models.LocalPreferences

class UpdateLocalPreferencesUseCase(
    private val localPreferencesRepository: LocalPreferencesRepository,
) {
    suspend operator fun invoke(localPreferences: LocalPreferences) =
        localPreferencesRepository.updateLocalPreferences(localPreferences)
}

class GetLocalPreferencesUseCase(
    private val localPreferencesRepository: LocalPreferencesRepository,
) {
    operator fun invoke() = localPreferencesRepository.getLocalPreferences()
}
