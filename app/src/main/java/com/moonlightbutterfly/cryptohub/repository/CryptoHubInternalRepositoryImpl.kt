package com.moonlightbutterfly.cryptohub.repository

import androidx.lifecycle.map
import com.moonlightbutterfly.cryptohub.repository.dataobjects.AppConfig
import database.daos.AppConfigDao
import javax.inject.Inject

/**
 * Internal app repository living inside the device.
 */
class CryptoHubInternalRepositoryImpl @Inject constructor(private val appConfigDao: AppConfigDao) :
    CryptoHubInternalRepository {

    override val appConfig = appConfigDao
        .getAll()
        .map { it.first() }

    override suspend fun updateAppConfig(appConfig: AppConfig) {
        appConfigDao.update(appConfig)
    }
}
