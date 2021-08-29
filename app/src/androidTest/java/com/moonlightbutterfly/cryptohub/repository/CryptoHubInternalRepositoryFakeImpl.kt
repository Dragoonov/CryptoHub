package com.moonlightbutterfly.cryptohub.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import database.AppConfig
import javax.inject.Inject

class CryptoHubInternalRepositoryFakeImpl @Inject constructor() : CryptoHubInternalRepository {

    override val appConfig: LiveData<AppConfig> = liveData { AppConfig() }

    override suspend fun updateAppConfig(appConfig: AppConfig) {
        // EMPTY
    }
}
