package com.moonlightbutterfly.cryptohub.repository

import com.moonlightbutterfly.cryptohub.data.LocalPreferencesDataSource
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeLocalPreferencesDataSourceImpl : LocalPreferencesDataSource {

    private var localPreferences = LocalPreferences.DEFAULT
    private val localPreferencesFlow = MutableStateFlow(localPreferences)

    override fun getLocalPreferences(): Flow<LocalPreferences> {
        return localPreferencesFlow
    }

    override suspend fun updateLocalPreferences(localPreferences: LocalPreferences) {
        this.localPreferences = localPreferences
        localPreferencesFlow.value = localPreferences.copy()
    }
}
