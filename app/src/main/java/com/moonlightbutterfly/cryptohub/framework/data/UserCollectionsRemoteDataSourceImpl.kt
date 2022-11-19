package com.moonlightbutterfly.cryptohub.framework.data

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.moonlightbutterfly.cryptohub.data.Error
import com.moonlightbutterfly.cryptohub.data.ErrorMapper
import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.data.UserCollectionsDataSource
import com.moonlightbutterfly.cryptohub.data.unpack
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await

/**
 * Data source using the Firestore database. It registers to the database and converts the
 * incoming data into flows.
 */
class UserCollectionsRemoteDataSourceImpl(
    db: FirebaseFirestore,
    userId: String,
    private val errorMapper: ErrorMapper
) : UserCollectionsDataSource {

    private val subscribedUserCollections = mutableMapOf<String, MutableStateFlow<Result<CryptoCollection>>>()
    private val userDocument = db.collection(USERS_COLLECTION).document(userId)
    private val allCollectionNames = MutableStateFlow<Result<List<String>>>(Result.Success(emptyList()))

    init {
        userDocument.addSnapshotListener { value, _ ->
            value?.data?.keys?.toList()?.let {
                if (allCollectionNames.value != it) {
                    allCollectionNames.value = Result.Success(it)
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
                    if (subscribedUserCollections[key]?.value?.unpack(CryptoCollection.EMPTY) != collection) {
                        subscribedUserCollections[key]?.value = Result.Success(collection)
                    }
                }
            }
        }
    }

    override fun getAllCollectionNames(): Flow<Result<List<String>>> {
        return allCollectionNames
    }

    override fun getCollection(name: String): Flow<Result<CryptoCollection>> {
        return subscribedUserCollections.getOrPut(name) {
            MutableStateFlow(Result.Success(CryptoCollection.EMPTY))
        }
    }

    override suspend fun clearCollection(name: String): Result<Unit> {
        return userDocument.update(name, emptyList<CryptoAsset>()).getTaskResult()
    }

    override suspend fun createCollection(name: String): Result<Unit> {
        return userDocument.update(name, emptyList<List<CryptoAsset>>()).getTaskResult()
    }

    override suspend fun removeCollection(name: String): Result<Unit> {
        return userDocument.update(name, FieldValue.delete()).getTaskResult()
    }

    override suspend fun addToCollection(asset: CryptoAsset, collectionName: String): Result<Unit> {
        return userDocument.update(collectionName, FieldValue.arrayUnion(asset)).getTaskResult()
    }

    override suspend fun removeFromCollection(asset: CryptoAsset, collectionName: String): Result<Unit> {
        return userDocument.update(collectionName, FieldValue.arrayRemove(asset)).getTaskResult()
    }

    private suspend fun Task<Void>.getTaskResult(): Result<Unit> {
        await()
        return if (isSuccessful) {
            Result.Success(Unit)
        } else {
            exception?.let {
                Result.Failure(errorMapper.mapError(it))
            } ?: Result.Failure(Error.Unknown("Unknown error getting task"))
        }
    }

    companion object {
        const val USERS_COLLECTION = "users"
    }
}
