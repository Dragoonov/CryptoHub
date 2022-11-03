package com.moonlightbutterfly.cryptohub.data

import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import kotlinx.coroutines.flow.Flow

interface LocalPreferencesDataSource {

    fun getLocalPreferences(): Flow<Result<LocalPreferences>>

    suspend fun updateLocalPreferences(localPreferences: LocalPreferences): Result<Unit>
}
