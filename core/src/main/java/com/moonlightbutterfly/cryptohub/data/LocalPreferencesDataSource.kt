package com.moonlightbutterfly.cryptohub.data

import com.moonlightbutterfly.cryptohub.domain.models.LocalPreferences
import kotlinx.coroutines.flow.Flow

interface LocalPreferencesDataSource {

    fun getLocalPreferences(): Flow<LocalPreferences>

    suspend fun updateLocalPreferences(localPreferences: LocalPreferences)
}
