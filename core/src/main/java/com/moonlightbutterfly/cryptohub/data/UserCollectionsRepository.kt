package com.moonlightbutterfly.cryptohub.data

import com.moonlightbutterfly.cryptohub.models.CryptoAsset

/**
 * Repository class aggregating external data sources and providing coherent interface for application.
 */
class UserCollectionsRepository(
    private val userCollectionsDataSource: UserCollectionsDataSource
) {
    fun getCollection(name: String) = userCollectionsDataSource.getCollection(name)

    fun getAllCollectionNames() = userCollectionsDataSource.getAllCollectionNames()

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
