package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import kotlinx.coroutines.flow.Flow

fun interface UpdateLocalPreferencesUseCase : suspend (LocalPreferences) -> Answer<Unit>
fun interface GetLocalPreferencesUseCase : () -> Flow<Answer<LocalPreferences>>
