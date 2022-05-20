package com.moonlightbutterfly.cryptohub.data

import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.domain.models.CryptoCollection
import kotlinx.coroutines.flow.Flow

/**
 * Repository class aggregating external data sources and providing coherent interface for application.
 */
class UserCollectionsRepository(
    private val userCollectionsDataSource: UserCollectionsDataSource
) {
    fun getCollection(name: String): Flow<CryptoCollection> = userCollectionsDataSource.getCollection(name)

    fun getAllCollectionNames(): Flow<List<String>> = userCollectionsDataSource.getAllCollectionNames()

    suspend fun clearCollection(name: String) = userCollectionsDataSource.clearCollection(name)

    suspend fun createCollection(name: String) = userCollectionsDataSource.createCollection(name)

    suspend fun removeCollection(name: String) = userCollectionsDataSource.removeCollection(name)

    suspend fun addToCollection(asset: CryptoAsset, collectionName: String) = userCollectionsDataSource.addToCollection(asset, collectionName)

    suspend fun removeFromCollection(asset: CryptoAsset, collectionName: String) = userCollectionsDataSource.removeFromCollection(asset, collectionName)

    companion object {
        const val FAVOURITES_COLLECTION_NAME = "favourites"
        const val RECENTS_COLLECTION_NAME = "recents"
    }
}
