package com.moonlightbutterfly.cryptohub.data.collections

import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeUserCollectionsLocalDataSourceImpl @Inject constructor() :
    UserCollectionsLocalDataSource {

    private val collections = mutableMapOf(
        "favourites" to MutableStateFlow(Answer.Success(CryptoCollection.EMPTY)),
        "recents" to MutableStateFlow(Answer.Success(CryptoCollection.EMPTY))
    )

    override suspend fun clearCollection(name: String): Answer<Unit> {
        collections[name]?.value = Answer.Success(CryptoCollection(name, emptyList()))
        return Answer.Success(Unit)
    }

    override fun getCollection(name: String): Flow<Answer<CryptoCollection>> {
        return collections[name] as Flow<Answer<CryptoCollection>>
    }

    override fun getAllCollectionNames(): Flow<Answer<List<String>>> {
        return flowOf(Answer.Success(collections.keys.toList()))
    }

    override suspend fun createCollection(name: String): Answer<Unit> {
        collections[name] = MutableStateFlow(Answer.Success(CryptoCollection(name, emptyList())))
        return Answer.Success(Unit)
    }

    override suspend fun removeCollection(name: String): Answer<Unit> {
        collections.remove(name)
        return Answer.Success(Unit)
    }

    override suspend fun addToCollection(asset: CryptoAsset, collectionName: String): Answer<Unit> {
        var collection = collections[collectionName]?.value
        if (collection == null) {
            createCollection(collectionName)
        }
        collection = collections[collectionName]!!.value
        collections[collectionName]!!.value = Answer.Success(CryptoCollection(collectionName, collection.data.cryptoAssets + asset))
        return Answer.Success(Unit)
    }

    override suspend fun removeFromCollection(asset: CryptoAsset, collectionName: String): Answer<Unit> {
        val collection = collections[collectionName]!!.value
        collections[collectionName]!!.value = Answer.Success(CryptoCollection(collectionName, collection.data.cryptoAssets - asset))
        return Answer.Success(Unit)
    }
}
