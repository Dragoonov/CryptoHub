package com.moonlightbutterfly.cryptohub.data.localpreferences

import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeLocalPreferencesDataSourceImpl : LocalPreferencesDataSource {

    private var localPreferences = LocalPreferences.DEFAULT
    private val localPreferencesFlow = MutableStateFlow(Result.Success(localPreferences))

    override fun getLocalPreferences(): Flow<Result<LocalPreferences>> {
        return localPreferencesFlow
    }

    override suspend fun updateLocalPreferences(localPreferences: LocalPreferences): Result<Unit> {
        this.localPreferences = localPreferences
        localPreferencesFlow.value = Result.Success(localPreferences.copy())
        return Result.Success(Unit)
    }
}
