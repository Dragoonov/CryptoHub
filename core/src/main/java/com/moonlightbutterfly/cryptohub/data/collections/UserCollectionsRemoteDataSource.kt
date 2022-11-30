package com.moonlightbutterfly.cryptohub.data.collections

import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import kotlinx.coroutines.flow.Flow
import com.moonlightbutterfly.cryptohub.data.common.Result

interface UserCollectionsRemoteDataSource {

    suspend fun clearCollection(userId: String, name: String): Result<Unit>

    fun getCollection(userId: String, name: String): Flow<Result<CryptoCollection>>

    fun getAllCollectionNames(userId: String): Flow<Result<List<String>>>

    suspend fun createCollection(userId: String, name: String): Result<Unit>

    suspend fun removeCollection(userId: String, name: String): Result<Unit>

    suspend fun addToCollection(userId: String, asset: CryptoAsset, collectionName: String): Result<Unit>

    suspend fun removeFromCollection(userId: String, asset: CryptoAsset, collectionName: String): Result<Unit>
}
