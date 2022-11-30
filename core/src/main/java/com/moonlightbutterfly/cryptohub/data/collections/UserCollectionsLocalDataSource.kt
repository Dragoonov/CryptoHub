package com.moonlightbutterfly.cryptohub.data.collections

import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import kotlinx.coroutines.flow.Flow

interface UserCollectionsLocalDataSource {

    suspend fun clearCollection(name: String): Result<Unit>

    fun getCollection(name: String): Flow<Result<CryptoCollection>>

    fun getAllCollectionNames(): Flow<Result<List<String>>>

    suspend fun createCollection(name: String): Result<Unit>

    suspend fun removeCollection(name: String): Result<Unit>

    suspend fun addToCollection(asset: CryptoAsset, collectionName: String): Result<Unit>

    suspend fun removeFromCollection(asset: CryptoAsset, collectionName: String): Result<Unit>
}
