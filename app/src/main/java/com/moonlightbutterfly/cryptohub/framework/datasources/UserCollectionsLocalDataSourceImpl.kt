package com.moonlightbutterfly.cryptohub.framework.datasources

import com.moonlightbutterfly.cryptohub.data.UserCollectionsDataSource
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.domain.models.CryptoCollection
import com.moonlightbutterfly.cryptohub.framework.database.daos.CryptoCollectionsDao
import com.moonlightbutterfly.cryptohub.framework.database.entities.CryptoCollectionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Data source using the Room database.
 */
class UserCollectionsLocalDataSourceImpl(
    private val cryptoCollectionsDao: CryptoCollectionsDao
) : UserCollectionsDataSource {

    override fun getCollection(name: String): Flow<CryptoCollection> =
        cryptoCollectionsDao.getCollectionByName(name)
            .map { list ->
                list.map { CryptoCollection(it.name, it.assets) }.firstOrNull()
                    ?: CryptoCollection.EMPTY
            }

    override fun getAllCollectionNames(): Flow<List<String>> = cryptoCollectionsDao.getAllCollectionNames()

    private suspend fun getCollectionEntity(name: String): CryptoCollectionEntity? =
        cryptoCollectionsDao.getCollectionByName(name).first().firstOrNull()

    override suspend fun clearCollection(name: String) {
        val collectionId = getCollectionEntity(name)?.id
        collectionId?.let {
            cryptoCollectionsDao.update(
                CryptoCollectionEntity(
                    id = collectionId,
                    name = name,
                    assets = emptyList()
                )
            )
        }
    }

    override suspend fun createCollection(name: String) =
        cryptoCollectionsDao.insert(CryptoCollectionEntity(name = name, assets = emptyList()))

    override suspend fun removeCollection(name: String) = cryptoCollectionsDao.remove(name)

    override suspend fun addToCollection(asset: CryptoAsset, collectionName: String) {
        val collection = getCollectionEntity(collectionName)
        if (collection == null) {
            createCollection(collectionName)
        }
        updateCollection(asset, collectionName, Collection<CryptoAsset>::plus)
    }

    override suspend fun removeFromCollection(asset: CryptoAsset, collectionName: String) {
        updateCollection(asset, collectionName, Collection<CryptoAsset>::minus)
    }

    private suspend fun updateCollection(
        cryptoAsset: CryptoAsset,
        collectionName: String,
        operation: Collection<CryptoAsset>.(CryptoAsset) -> List<CryptoAsset>
    ) {
        val collection = getCollectionEntity(collectionName)!!
        val newCollection = CryptoCollectionEntity(
            collection.id,
            collectionName,
            collection.assets.operation(cryptoAsset)
        )
        cryptoCollectionsDao.update(newCollection)
    }
}
