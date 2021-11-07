package com.moonlightbutterfly.cryptohub.framework

import com.moonlightbutterfly.cryptohub.data.UserConfigurationDataSource
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.domain.models.UserSettings
import com.moonlightbutterfly.cryptohub.framework.database.daos.FavouritesDao
import com.moonlightbutterfly.cryptohub.framework.database.daos.UserSettingsDao
import com.moonlightbutterfly.cryptohub.framework.database.entities.FavouriteEntity
import com.moonlightbutterfly.cryptohub.framework.database.entities.UserSettingsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserConfigurationDataSourceImpl(
    private val userSettingsDao: UserSettingsDao,
    private val favouritesDao: FavouritesDao
) : UserConfigurationDataSource {

    override fun getFavourites(): Flow<List<CryptoAsset>> = favouritesDao.getAll()
        .map { list -> list.map { it.asset } }

    override suspend fun addFavourite(asset: CryptoAsset) = favouritesDao.insert(FavouriteEntity(asset = asset))

    override suspend fun removeFavourite(asset: CryptoAsset) = favouritesDao.remove(asset)

    override fun getUserSettings(): Flow<UserSettings> = userSettingsDao.getAll().map { it.first().settings }

    override suspend fun updateUserSettings(userSettings: UserSettings) = userSettingsDao.update(
        UserSettingsEntity(settings = userSettings)
    )
}
