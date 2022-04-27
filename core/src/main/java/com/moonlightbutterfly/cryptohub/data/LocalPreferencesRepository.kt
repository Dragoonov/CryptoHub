package com.moonlightbutterfly.cryptohub.data

import com.moonlightbutterfly.cryptohub.domain.models.LocalPreferences
import kotlinx.coroutines.flow.Flow

/**
 * Repository class aggregating local data sources and providing coherent interface for application.
 */
class LocalPreferencesRepository(
    private val localPreferencesDataSource: LocalPreferencesDataSource
) {
    suspend fun updateLocalPreferences(localPreferences: LocalPreferences) = localPreferencesDataSource.updateLocalPreferences(localPreferences)

    fun getLocalPreferences(): Flow<LocalPreferences> = localPreferencesDataSource.getLocalPreferences()
}
