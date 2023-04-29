package com.moonlightbutterfly.cryptohub.collections

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.moonlightbutterfly.cryptohub.data.collections.UserCollectionsRemoteDataSource
import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.data.common.Error
import com.moonlightbutterfly.cryptohub.data.common.ErrorMapper
import com.moonlightbutterfly.cryptohub.data.common.unpack
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Data source using the Firestore database. It registers to the database and converts the
 * incoming data into flows.
 */
class UserCollectionsRemoteDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val errorMapper: ErrorMapper
) : UserCollectionsRemoteDataSource {

    private val subscribedUsersCollections =
        mutableMapOf<String, MutableMap<String, MutableStateFlow<Answer<CryptoCollection>>>>()
    private val usersDocuments = mutableMapOf<String, DocumentReference>()
    private val allUsersCollectionNames =
        mutableMapOf<String, MutableStateFlow<Answer<List<String>>>>()
    private val trackedUserIds = mutableListOf<String>()

    private fun initializeForUser(userId: String) {
        if (!trackedUserIds.contains(userId)) {
            trackedUserIds.add(userId)

            val userCollections = subscribedUsersCollections.getOrPut(userId) {
                mutableMapOf()
            }
            val userDocument = usersDocuments.getOrPut(userId) {
                db.collection(USERS_COLLECTION).document(userId)
            }
            val collections = allUsersCollectionNames.getOrPut(userId) {
                MutableStateFlow(Answer.Success(emptyList()))
            }

            userDocument.addSnapshotListener { value, _ ->
                value?.data?.keys?.toList()?.let {
                    if (collections.value != it) {
                        collections.value = Answer.Success(it)
                    }
                }
                userCollections.keys.forEach { key ->
                    val assets = (value?.get(key) as List<Map<String, String>>?)?.map {
                        CryptoAsset(
                            name = it["name"] as String,
                            symbol = it["symbol"] as String,
                            logoUrl = it["logoUrl"] as String
                        )
                    }
                    if (assets != null) {
                        val collection = CryptoCollection(name = key, assets)
                        val unpacked = userCollections[key]?.value?.unpack(CryptoCollection.EMPTY)
                        if (unpacked != collection) {
                            userCollections[key]?.value = Answer.Success(collection)
                        }
                    }
                }
            }
        }
    }

    override fun getAllCollectionNames(userId: String): StateFlow<Answer<List<String>>> {
        initializeForUser(userId)
        return allUsersCollectionNames.getValue(userId)
    }

    override fun getCollection(userId: String, name: String): StateFlow<Answer<CryptoCollection>> {
        initializeForUser(userId)
        return subscribedUsersCollections.getValue(userId).getOrPut(name) {
            MutableStateFlow(Answer.Success(CryptoCollection.EMPTY))
        }
    }

    override suspend fun clearCollection(userId: String, name: String): Answer<Unit> {
        initializeForUser(userId)
        return usersDocuments
            .getValue(userId)
            .update(name, emptyList<CryptoAsset>())
            .getTaskResult()
    }

    override suspend fun createCollection(userId: String, name: String): Answer<Unit> {
        initializeForUser(userId)
        return usersDocuments
            .getValue(userId)
            .update(name, emptyList<List<CryptoAsset>>())
            .getTaskResult()
    }

    override suspend fun removeCollection(userId: String, name: String): Answer<Unit> {
        initializeForUser(userId)
        return usersDocuments.getValue(userId).update(name, FieldValue.delete()).getTaskResult()
    }

    override suspend fun addToCollection(
        userId: String,
        asset: CryptoAsset,
        collectionName: String
    ): Answer<Unit> {
        initializeForUser(userId)
        return usersDocuments
            .getValue(userId)
            .update(collectionName, FieldValue.arrayUnion(asset))
            .getTaskResult()
    }

    override suspend fun removeFromCollection(
        userId: String,
        asset: CryptoAsset,
        collectionName: String
    ): Answer<Unit> {
        initializeForUser(userId)
        return usersDocuments
            .getValue(userId)
            .update(collectionName, FieldValue.arrayRemove(asset))
            .getTaskResult()
    }

    private suspend fun Task<Void>.getTaskResult(): Answer<Unit> {
        await()
        return if (isSuccessful) {
            Answer.Success(Unit)
        } else {
            exception?.let {
                Answer.Failure(errorMapper.mapError(it))
            } ?: Answer.Failure(Error.Unknown("Unknown error getting task"))
        }
    }

    companion object {
        const val USERS_COLLECTION = "users"
    }
}
