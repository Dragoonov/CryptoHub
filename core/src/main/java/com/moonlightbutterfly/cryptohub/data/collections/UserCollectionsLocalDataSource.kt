package com.moonlightbutterfly.cryptohub.data.collections

import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import kotlinx.coroutines.flow.Flow

interface UserCollectionsLocalDataSource {

    suspend fun clearCollection(name: String): Answer<Unit>

    fun getCollection(name: String): Flow<Answer<CryptoCollection>>

    fun getAllCollectionNames(): Flow<Answer<List<String>>>

    suspend fun createCollection(name: String): Answer<Unit>

    suspend fun removeCollection(name: String): Answer<Unit>

    suspend fun addToCollection(asset: CryptoAsset, collectionName: String): Answer<Unit>

    suspend fun removeFromCollection(asset: CryptoAsset, collectionName: String): Answer<Unit>
}
