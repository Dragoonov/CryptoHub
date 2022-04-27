package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.LocalPreferencesRepository
import com.moonlightbutterfly.cryptohub.domain.models.LocalPreferences

class UpdateLocalPreferencesUseCase(private val localPreferencesRepository: LocalPreferencesRepository) {
    suspend operator fun invoke(localPreferences: LocalPreferences) = localPreferencesRepository.updateLocalPreferences(localPreferences)
}
