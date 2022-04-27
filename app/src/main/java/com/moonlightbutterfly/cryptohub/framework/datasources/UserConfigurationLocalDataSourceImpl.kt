package com.moonlightbutterfly.cryptohub.framework.datasources

import com.moonlightbutterfly.cryptohub.data.UserConfigurationDataSource
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.framework.database.daos.FavouritesDao
import com.moonlightbutterfly.cryptohub.framework.database.daos.RecentsDao
import com.moonlightbutterfly.cryptohub.framework.database.entities.FavouriteEntity
import com.moonlightbutterfly.cryptohub.framework.database.entities.RecentEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Local data source using android's Room database focusing on user specific data, eg. favourites list.
 */
class UserConfigurationLocalDataSourceImpl(
    private val favouritesDao: FavouritesDao,
    private val recentsDao: RecentsDao
) : UserConfigurationDataSource {

    override fun getFavourites(): Flow<List<CryptoAsset>> = favouritesDao.getAll()
        .map { list -> list.map { it.asset } }

    override fun getRecents(): Flow<List<CryptoAsset>> =
        recentsDao.getAll().map { list -> list.map { it.asset } }

    override suspend fun addFavourite(asset: CryptoAsset) = favouritesDao.insert(FavouriteEntity(asset = asset))

    override suspend fun addRecent(asset: CryptoAsset) = recentsDao.insert(RecentEntity(asset = asset))

    override suspend fun removeRecents() = recentsDao.removeAll()

    override suspend fun removeFavourite(asset: CryptoAsset) = favouritesDao.remove(asset)

    override suspend fun removeRecent(asset: CryptoAsset) = recentsDao.remove(asset)
}
