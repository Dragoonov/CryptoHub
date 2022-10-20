package com.moonlightbutterfly.cryptohub.data

import com.moonlightbutterfly.cryptohub.models.LocalPreferences

/**
 * Repository class aggregating local data sources and providing coherent interface for application.
 */
class LocalPreferencesRepository(
    private val localPreferencesDataSource: LocalPreferencesDataSource
) {
    suspend fun updateLocalPreferences(localPreferences: LocalPreferences) = localPreferencesDataSource.updateLocalPreferences(localPreferences)

    fun getLocalPreferences() = localPreferencesDataSource.getLocalPreferences()
}
