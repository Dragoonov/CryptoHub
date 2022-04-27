package com.moonlightbutterfly.cryptohub.framework.datasources

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.moonlightbutterfly.cryptohub.data.UserConfigurationDataSource
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Data source using the Firestore database. It registers to the database and converts the
 * incoming data into flows.
 */
class UserConfigurationRemoteDataSourceImpl(
    private val db: FirebaseFirestore
) : UserConfigurationDataSource {

    private val favouritesFlow = MutableStateFlow<List<CryptoAsset>>(emptyList())
    private val recentsFlow = MutableStateFlow<List<CryptoAsset>>(emptyList())
    private var listener: ListenerRegistration? = null
    private var userId: String = ""

    fun registerFor(userId: String) {
        if (userId == this.userId) {
            return
        }
        this.userId = userId
        favouritesFlow.value = emptyList()
        recentsFlow.value = emptyList()
        listener?.remove()
        db.collection(USERS_COLLECTION).document(userId).apply {
            get().addOnCompleteListener { task ->
                if (!task.result.exists()) {
                    initializeUserCryptoLists()
                }
                listener = addSnapshotListener { value, _ -> value?.publishUserLists() }
            }
        }
    }

    private fun DocumentReference.initializeUserCryptoLists() {
        val initialValue = mapOf<String, List<CryptoAsset>>(
            FAVOURITES_FIELD to emptyList(),
            RECENTS_FIELD to emptyList(),
        )
        set(initialValue)
    }

    private fun DocumentSnapshot.publishUserLists() {
        val recentsList = getRecents()
        if (recentsList != recentsFlow.value) {
            recentsFlow.value = recentsList
        }
        val favouritesList = getFavourites()
        if (favouritesList != favouritesFlow.value) {
            favouritesFlow.value = favouritesList
        }
    }

    private fun DocumentSnapshot.getRecents(): List<CryptoAsset> {
        return (get(RECENTS_FIELD) as List<Map<String, String>>).map {
            CryptoAsset(
                name = it["name"] as String,
                symbol = it["symbol"] as String,
                logoUrl = it["logoUrl"] as String
            )
        }
    }

    private fun DocumentSnapshot.getFavourites(): List<CryptoAsset> {
        return (get(FAVOURITES_FIELD) as List<Map<String, String>>).map {
            CryptoAsset(
                name = it["name"] as String,
                symbol = it["symbol"] as String,
                logoUrl = it["logoUrl"] as String
            )
        }
    }

    override fun getFavourites(): Flow<List<CryptoAsset>> = favouritesFlow

    override fun getRecents(): Flow<List<CryptoAsset>> = recentsFlow

    override suspend fun addFavourite(asset: CryptoAsset): Unit = run {
        db
            .collection(USERS_COLLECTION)
            .document(userId)
            .update(FAVOURITES_FIELD, FieldValue.arrayUnion(asset))
    }

    override suspend fun addRecent(asset: CryptoAsset): Unit = run {
        db
            .collection(USERS_COLLECTION)
            .document(userId)
            .update(RECENTS_FIELD, FieldValue.arrayUnion(asset))
    }

    override suspend fun removeRecents(): Unit = run {
        db
            .collection(USERS_COLLECTION)
            .document(userId)
            .update(RECENTS_FIELD, emptyList<CryptoAsset>())
    }

    override suspend fun removeFavourite(asset: CryptoAsset): Unit = run {
        db
            .collection(USERS_COLLECTION)
            .document(userId)
            .update(FAVOURITES_FIELD, FieldValue.arrayRemove(asset))
    }

    override suspend fun removeRecent(asset: CryptoAsset): Unit = run {
        db
            .collection(USERS_COLLECTION)
            .document(userId)
            .update(RECENTS_FIELD, FieldValue.arrayRemove(asset))
    }

    companion object {
        const val USERS_COLLECTION = "users"
        const val RECENTS_FIELD = "recents"
        const val FAVOURITES_FIELD = "favourites"
    }
}
