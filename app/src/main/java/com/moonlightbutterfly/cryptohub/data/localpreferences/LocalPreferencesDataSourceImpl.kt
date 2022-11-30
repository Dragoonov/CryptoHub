package com.moonlightbutterfly.cryptohub.data.localpreferences

import com.moonlightbutterfly.cryptohub.data.common.Error
import com.moonlightbutterfly.cryptohub.data.common.ErrorMapper
import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.data.database.daos.LocalPreferencesDao
import com.moonlightbutterfly.cryptohub.data.database.entities.LocalPreferencesEntity
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Local data source using android's Room database, focusing on device specific preferences, eg. night mode.
 */
class LocalPreferencesDataSourceImpl(
    private val localPreferencesDao: LocalPreferencesDao,
    private val errorMapper: ErrorMapper
) : LocalPreferencesDataSource {

    override fun getLocalPreferences(): Flow<Result<LocalPreferences>> =
        localPreferencesDao.getAll()
            .map {
                Result.Success(it.first().preferences) as Result<LocalPreferences>
            }
            .catch {
                emit(Result.Failure(errorMapper.mapError(it)))
            }

    override suspend fun updateLocalPreferences(localPreferences: LocalPreferences): Result<Unit> {
        return localPreferencesDao.update(
            LocalPreferencesEntity(preferences = localPreferences)
        ).let {
            if (it > 0) {
                Result.Success(Unit)
            } else {
                Result.Failure(Error.NotFound("Local preferences not found"))
            }
        }
    }
}
