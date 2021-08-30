package com.moonlightbutterfly.cryptohub.repository

import androidx.lifecycle.LiveData
import com.moonlightbutterfly.cryptohub.repository.dataobjects.AppConfig

interface CryptoHubInternalRepository {

    val appConfig: LiveData<AppConfig>

    suspend fun updateAppConfig(appConfig: AppConfig)
}
