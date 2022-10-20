package com.moonlightbutterfly.cryptohub.repository

import com.moonlightbutterfly.cryptohub.data.UserCollectionsDataSource
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeUserCollectionsDataSourceImpl : UserCollectionsDataSource {

    private val collections = mutableMapOf(
        "favourites" to MutableStateFlow(CryptoCollection.EMPTY),
        "recents" to MutableStateFlow(CryptoCollection.EMPTY)
    )

    override suspend fun clearCollection(name: String) {
        collections[name]?.value = CryptoCollection(name, emptyList())
    }

    override fun getCollection(name: String): Flow<CryptoCollection> {
        return collections[name] as Flow<CryptoCollection>
    }

    override fun getAllCollectionNames(): Flow<List<String>> {
        return flowOf(collections.keys.toList())
    }

    override suspend fun createCollection(name: String) {
        collections[name] = MutableStateFlow(CryptoCollection(name, emptyList()))
    }

    override suspend fun removeCollection(name: String) {
        collections.remove(name)
    }

    override suspend fun addToCollection(asset: CryptoAsset, collectionName: String) {
        var collection = collections[collectionName]?.value
        if (collection == null) {
            createCollection(collectionName)
        }
        collection = collections[collectionName]!!.value
        collections[collectionName]!!.value = CryptoCollection(collectionName, collection.cryptoAssets + asset)
    }

    override suspend fun removeFromCollection(asset: CryptoAsset, collectionName: String) {
        val collection = collections[collectionName]!!.value
        collections[collectionName]!!.value = CryptoCollection(collectionName, collection.cryptoAssets - asset)
    }
}
