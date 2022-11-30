package com.moonlightbutterfly.cryptohub.data.localpreferences

import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import kotlinx.coroutines.flow.Flow

interface LocalPreferencesDataSource {

    fun getLocalPreferences(): Flow<Result<LocalPreferences>>

    suspend fun updateLocalPreferences(localPreferences: LocalPreferences): Result<Unit>
}
