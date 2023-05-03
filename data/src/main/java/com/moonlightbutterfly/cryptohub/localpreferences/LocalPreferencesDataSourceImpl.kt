package com.moonlightbutterfly.cryptohub.localpreferences

import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.data.common.Error
import com.moonlightbutterfly.cryptohub.data.common.ErrorMapper
import com.moonlightbutterfly.cryptohub.data.localpreferences.LocalPreferencesDataSource
import com.moonlightbutterfly.cryptohub.database.daos.LocalPreferencesDao
import com.moonlightbutterfly.cryptohub.database.dtos.toLocalPreferences
import com.moonlightbutterfly.cryptohub.database.dtos.toLocalPreferencesDto
import com.moonlightbutterfly.cryptohub.database.entities.LocalPreferencesEntity
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Local data source using android's Room database, focusing on device specific preferences, eg. night mode.
 */
internal class LocalPreferencesDataSourceImpl @Inject constructor(
    private val localPreferencesDao: LocalPreferencesDao,
    private val errorMapper: ErrorMapper
) : LocalPreferencesDataSource {

    override fun getLocalPreferences(): Flow<Answer<LocalPreferences>> =
        localPreferencesDao.getAll()
            .map {
                Answer.Success(it.first().preferences.toLocalPreferences()) as Answer<LocalPreferences>
            }
            .catch {
                emit(Answer.Failure(errorMapper.mapError(it)))
            }

    override suspend fun updateLocalPreferences(localPreferences: LocalPreferences): Answer<Unit> {
        return localPreferencesDao.update(
            LocalPreferencesEntity(preferences = localPreferences.toLocalPreferencesDto())
        ).let {
            if (it > 0) {
                Answer.Success(Unit)
            } else {
                Answer.Failure(Error.NotFound("Local preferences not found"))
            }
        }
    }
}
