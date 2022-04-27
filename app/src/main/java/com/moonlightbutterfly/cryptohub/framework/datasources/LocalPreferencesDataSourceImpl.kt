package com.moonlightbutterfly.cryptohub.framework.datasources

import com.moonlightbutterfly.cryptohub.data.LocalPreferencesDataSource
import com.moonlightbutterfly.cryptohub.domain.models.LocalPreferences
import com.moonlightbutterfly.cryptohub.framework.database.daos.LocalPreferencesDao
import com.moonlightbutterfly.cryptohub.framework.database.entities.LocalPreferencesEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Local data source using android's Room database, focusing on device specific preferences, eg. night mode.
 */
class LocalPreferencesDataSourceImpl(
    private val localPreferencesDao: LocalPreferencesDao,
) : LocalPreferencesDataSource {

    override fun getLocalPreferences(): Flow<LocalPreferences> =
        localPreferencesDao.getAll().map { it.first().preferences }

    override suspend fun updateLocalPreferences(localPreferences: LocalPreferences) {
        localPreferencesDao.update(
            LocalPreferencesEntity(preferences = localPreferences)
        )
    }
}
