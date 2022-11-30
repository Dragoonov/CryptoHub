package com.moonlightbutterfly.cryptohub.data.collections

import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeUserCollectionsLocalDataSourceImpl : UserCollectionsLocalDataSource {

    private val collections = mutableMapOf(
        "favourites" to MutableStateFlow(Result.Success(CryptoCollection.EMPTY)),
        "recents" to MutableStateFlow(Result.Success(CryptoCollection.EMPTY))
    )

    override suspend fun clearCollection(name: String): Result<Unit> {
        collections[name]?.value = Result.Success(CryptoCollection(name, emptyList()))
        return Result.Success(Unit)
    }

    override fun getCollection(name: String): Flow<Result<CryptoCollection>> {
        return collections[name] as Flow<Result<CryptoCollection>>
    }

    override fun getAllCollectionNames(): Flow<Result<List<String>>> {
        return flowOf(Result.Success(collections.keys.toList()))
    }

    override suspend fun createCollection(name: String): Result<Unit> {
        collections[name] = MutableStateFlow(Result.Success(CryptoCollection(name, emptyList())))
        return Result.Success(Unit)
    }

    override suspend fun removeCollection(name: String): Result<Unit> {
        collections.remove(name)
        return Result.Success(Unit)
    }

    override suspend fun addToCollection(asset: CryptoAsset, collectionName: String): Result<Unit> {
        var collection = collections[collectionName]?.value
        if (collection == null) {
            createCollection(collectionName)
        }
        collection = collections[collectionName]!!.value
        collections[collectionName]!!.value = Result.Success(CryptoCollection(collectionName, collection.data.cryptoAssets + asset))
        return Result.Success(Unit)
    }

    override suspend fun removeFromCollection(asset: CryptoAsset, collectionName: String): Result<Unit> {
        val collection = collections[collectionName]!!.value
        collections[collectionName]!!.value = Result.Success(CryptoCollection(collectionName, collection.data.cryptoAssets - asset))
        return Result.Success(Unit)
    }
}
