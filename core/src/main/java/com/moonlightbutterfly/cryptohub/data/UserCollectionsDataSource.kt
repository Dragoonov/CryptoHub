package com.moonlightbutterfly.cryptohub.data

import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.domain.models.CryptoCollection
import kotlinx.coroutines.flow.Flow

interface UserCollectionsDataSource {

    suspend fun clearCollection(name: String)

    fun getCollection(name: String): Flow<CryptoCollection>

    fun getAllCollectionNames(): Flow<List<String>>

    suspend fun createCollection(name: String)

    suspend fun removeCollection(name: String)

    suspend fun addToCollection(asset: CryptoAsset, collectionName: String)

    suspend fun removeFromCollection(asset: CryptoAsset, collectionName: String)
}
