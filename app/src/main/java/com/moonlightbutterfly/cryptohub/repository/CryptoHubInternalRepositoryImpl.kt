package com.moonlightbutterfly.cryptohub.repository

import androidx.lifecycle.map
import com.moonlightbutterfly.cryptohub.database.daos.AppConfigDao
import com.moonlightbutterfly.cryptohub.database.daos.FavouritesDao
import com.moonlightbutterfly.cryptohub.dataobjects.AppConfig
import com.moonlightbutterfly.cryptohub.dataobjects.FavouriteCryptoasset
import javax.inject.Inject

/**
 * Internal app repository living inside the device.
 */
class CryptoHubInternalRepositoryImpl @Inject constructor(
    private val appConfigDao: AppConfigDao,
    private val favouritesDao: FavouritesDao) :
    CryptoHubInternalRepository {

    override val appConfig = appConfigDao
        .getAll()
        .map { it.first() }

    override val favourites = favouritesDao.getAll()

    override suspend fun updateAppConfig(appConfig: AppConfig) {
        appConfigDao.update(appConfig)
    }

    override suspend fun insertFavourite(favourite: FavouriteCryptoasset) {
        favouritesDao.insert(favourite)
    }

    override suspend fun removeFavourite(favourite: FavouriteCryptoasset) {
        favouritesDao.remove(favourite.symbol)
    }
}
