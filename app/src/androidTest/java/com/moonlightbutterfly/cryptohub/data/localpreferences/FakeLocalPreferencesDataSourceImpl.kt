package com.moonlightbutterfly.cryptohub.data.localpreferences

import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.data.localpreferences.LocalPreferencesDataSource
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class FakeLocalPreferencesDataSourceImpl @Inject constructor() : LocalPreferencesDataSource {

    private var localPreferences = LocalPreferences.DEFAULT
    private val localPreferencesFlow = MutableStateFlow(Answer.Success(localPreferences))

    override fun getLocalPreferences(): Flow<Answer<LocalPreferences>> {
        return localPreferencesFlow
    }

    override suspend fun updateLocalPreferences(localPreferences: LocalPreferences): Answer<Unit> {
        this.localPreferences = localPreferences
        localPreferencesFlow.value = Answer.Success(localPreferences.copy())
        return Answer.Success(Unit)
    }
}
