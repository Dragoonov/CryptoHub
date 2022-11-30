package com.moonlightbutterfly.cryptohub.data.collections

import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import com.moonlightbutterfly.cryptohub.data.common.Result

class FakeUserCollectionsRemoteDataSourceImpl : UserCollectionsRemoteDataSource {

    private val collections = mutableMapOf(
        "favourites" to MutableStateFlow(Result.Success(CryptoCollection.EMPTY)),
        "recents" to MutableStateFlow(Result.Success(CryptoCollection.EMPTY))
    )

    override suspend fun clearCollection(userId: String, name: String): Result<Unit> {
        collections[name]?.value = Result.Success(CryptoCollection(name, emptyList()))
        return Result.Success(Unit)
    }

    override fun getCollection(userId: String, name: String): Flow<Result<CryptoCollection>> {
        return collections[name] as Flow<Result<CryptoCollection>>
    }

    override fun getAllCollectionNames(userId: String): Flow<Result<List<String>>> {
        return flowOf(Result.Success(collections.keys.toList()))
    }

    override suspend fun createCollection(userId: String, name: String): Result<Unit> {
        collections[name] = MutableStateFlow(Result.Success(CryptoCollection(name, emptyList())))
        return Result.Success(Unit)
    }

    override suspend fun removeCollection(userId: String, name: String): Result<Unit> {
        collections.remove(name)
        return Result.Success(Unit)
    }

    override suspend fun addToCollection(userId: String, asset: CryptoAsset, collectionName: String): Result<Unit> {
        var collection = collections[collectionName]?.value
        if (collection == null) {
            createCollection(collectionName, userId)
        }
        collection = collections[collectionName]!!.value
        collections[collectionName]!!.value = Result.Success(CryptoCollection(collectionName, collection.data.cryptoAssets + asset))
        return Result.Success(Unit)
    }

    override suspend fun removeFromCollection(userId: String, asset: CryptoAsset, collectionName: String): Result<Unit> {
        val collection = collections[collectionName]!!.value
        collections[collectionName]!!.value = Result.Success(CryptoCollection(collectionName, collection.data.cryptoAssets - asset))
        return Result.Success(Unit)
    }
}
