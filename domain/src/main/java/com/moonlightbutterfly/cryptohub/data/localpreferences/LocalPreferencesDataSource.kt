package com.moonlightbutterfly.cryptohub.data.localpreferences

import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import kotlinx.coroutines.flow.Flow

interface LocalPreferencesDataSource {

    fun getLocalPreferences(): Flow<Answer<LocalPreferences>>

    suspend fun updateLocalPreferences(localPreferences: LocalPreferences): Answer<Unit>
}
