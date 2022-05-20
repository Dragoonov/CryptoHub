package com.moonlightbutterfly.cryptohub.framework.datasources

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.moonlightbutterfly.cryptohub.data.UserCollectionsDataSource
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.domain.models.CryptoCollection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Data source using the Firestore database. It registers to the database and converts the
 * incoming data into flows.
 */
class UserCollectionsRemoteDataSourceImpl(
    db: FirebaseFirestore,
    userId: String
) : UserCollectionsDataSource {

    private val subscribedUserCollections = mutableMapOf<String, MutableStateFlow<CryptoCollection>>()
    private val userDocument = db.collection(USERS_COLLECTION).document(userId)
    private val allCollectionNames = MutableStateFlow<List<String>>(emptyList())

    init {
        userDocument.addSnapshotListener { value, _ ->
            value?.data?.keys?.toList()?.let {
                if (allCollectionNames.value != it) {
                    allCollectionNames.value = it
                }
            }
            subscribedUserCollections.keys.forEach { key ->
                val assets = (value?.get(key) as List<Map<String, String>>?)?.map {
                    CryptoAsset(
                        name = it["name"] as String,
                        symbol = it["symbol"] as String,
                        logoUrl = it["logoUrl"] as String
                    )
                }
                if (assets != null) {
                    val collection = CryptoCollection(name = key, assets)
                    if (subscribedUserCollections[key]?.value != collection) {
                        subscribedUserCollections[key]?.value = collection
                    }
                }
            }
        }
    }

    override fun getAllCollectionNames(): Flow<List<String>> {
        return allCollectionNames
    }

    override fun getCollection(name: String): Flow<CryptoCollection> {
        if (subscribedUserCollections[name] == null) {
            subscribedUserCollections[name] = MutableStateFlow(CryptoCollection.EMPTY)
        }
        return subscribedUserCollections[name] as Flow<CryptoCollection>
    }

    override suspend fun clearCollection(name: String) {
        userDocument.update(name, emptyList<CryptoAsset>())
    }

    override suspend fun createCollection(name: String) {
        userDocument.update(name, emptyList<List<CryptoAsset>>())
    }

    override suspend fun removeCollection(name: String) {
        userDocument.update(name, FieldValue.delete())
    }

    override suspend fun addToCollection(asset: CryptoAsset, collectionName: String) {
        userDocument.update(collectionName, FieldValue.arrayUnion(asset))
    }

    override suspend fun removeFromCollection(asset: CryptoAsset, collectionName: String) {
        userDocument.update(collectionName, FieldValue.arrayRemove(asset))
    }

    companion object {
        const val USERS_COLLECTION = "users"
    }
}
