package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.LocalPreferencesRepository

class GetLocalPreferencesUseCase(private val localPreferencesRepository: LocalPreferencesRepository) {
    operator fun invoke() = localPreferencesRepository.getLocalPreferences()
}
