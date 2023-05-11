package com.moonlightbutterfly.cryptohub.data.collections

import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import kotlinx.coroutines.flow.Flow

interface UserCollectionsRemoteDataSource {

    suspend fun clearCollection(userId: String, name: String): Answer<Unit>

    fun getCollection(userId: String, name: String): Flow<Answer<CryptoCollection>>

    fun getAllCollectionNames(userId: String): Flow<Answer<List<String>>>

    suspend fun createCollection(userId: String, name: String): Answer<Unit>

    suspend fun removeCollection(userId: String, name: String): Answer<Unit>

    suspend fun addToCollection(userId: String, asset: CryptoAsset, collectionName: String): Answer<Unit>

    suspend fun removeFromCollection(userId: String, asset: CryptoAsset, collectionName: String): Answer<Unit>
}
