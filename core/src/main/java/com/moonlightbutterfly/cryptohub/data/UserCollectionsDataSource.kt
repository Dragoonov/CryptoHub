package com.moonlightbutterfly.cryptohub.data

import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import kotlinx.coroutines.flow.Flow

interface UserCollectionsDataSource {

    suspend fun clearCollection(name: String): Result<Unit>

    fun getCollection(name: String): Flow<Result<CryptoCollection>>

    fun getAllCollectionNames(): Flow<Result<List<String>>>

    suspend fun createCollection(name: String): Result<Unit>

    suspend fun removeCollection(name: String): Result<Unit>

    suspend fun addToCollection(asset: CryptoAsset, collectionName: String): Result<Unit>

    suspend fun removeFromCollection(asset: CryptoAsset, collectionName: String): Result<Unit>
}
