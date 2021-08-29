package com.moonlightbutterfly.cryptohub.repository

import androidx.lifecycle.LiveData
import database.AppConfig

interface CryptoHubInternalRepository {

    val appConfig: LiveData<AppConfig>

    suspend fun updateAppConfig(appConfig: AppConfig)
}
