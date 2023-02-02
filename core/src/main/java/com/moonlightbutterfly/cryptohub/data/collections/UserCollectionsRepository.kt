package com.moonlightbutterfly.cryptohub.data.collections

import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.data.common.getOrThrow
import com.moonlightbutterfly.cryptohub.data.user.UserDataSource
import com.moonlightbutterfly.cryptohub.models.CryptoAsset

/**
 * Repository class aggregating external data sources and providing coherent interface
 * for application.
 */
class UserCollectionsRepository(
    private val userCollectionsRemoteDataSource: UserCollectionsRemoteDataSource,
    private val userCollectionsLocalDataSource: UserCollectionsLocalDataSource,
    private val userDataSource: UserDataSource
) {
    fun getCollection(name: String) = if (userDataSource.isUserSignedIn().getOrThrow()) {
        userCollectionsRemoteDataSource.getCollection(
            userDataSource.getUser().getOrThrow().userId, name
        )
    } else {
        userCollectionsLocalDataSource.getCollection(name)
    }

    fun getAllCollectionNames() = if (userDataSource.isUserSignedIn().getOrThrow()) {
        userCollectionsRemoteDataSource.getAllCollectionNames(
            userDataSource.getUser().getOrThrow().userId
        )
    } else {
        userCollectionsLocalDataSource.getAllCollectionNames()
    }

    suspend fun clearCollection(name: String) = if (userDataSource.isUserSignedIn().getOrThrow()) {
        userCollectionsRemoteDataSource.clearCollection(
            userDataSource.getUser().getOrThrow().userId, name
        )
    } else {
        userCollectionsLocalDataSource.clearCollection(name)
    }

    suspend fun createCollection(name: String) = if (userDataSource.isUserSignedIn().getOrThrow()) {
        userCollectionsRemoteDataSource.createCollection(
            userDataSource.getUser().getOrThrow().userId, name
        )
    } else {
        userCollectionsLocalDataSource.createCollection(name)
    }

    suspend fun removeCollection(name: String) = if (userDataSource.isUserSignedIn().getOrThrow()) {
        userCollectionsRemoteDataSource.removeCollection(
            userDataSource.getUser().getOrThrow().userId, name
        )
    } else {
        userCollectionsLocalDataSource.removeCollection(name)
    }

    suspend fun addToCollection(asset: CryptoAsset, collectionName: String): Result<Unit> {
        return if (userDataSource.isUserSignedIn().getOrThrow()) {
            userCollectionsRemoteDataSource.addToCollection(
                userDataSource.getUser().getOrThrow().userId, asset, collectionName
            )
        } else {
            userCollectionsLocalDataSource.addToCollection(asset, collectionName)
        }
    }

    suspend fun removeFromCollection(asset: CryptoAsset, collectionName: String): Result<Unit> {
        return if (userDataSource.isUserSignedIn().getOrThrow()) {
            userCollectionsRemoteDataSource.removeFromCollection(
                userDataSource.getUser().getOrThrow().userId, asset, collectionName
            )
        } else {
            userCollectionsLocalDataSource.removeFromCollection(asset, collectionName)
        }
    }

    companion object {
        const val FAVOURITES_COLLECTION_NAME = "favourites"
        const val RECENTS_COLLECTION_NAME = "recents"
    }
}
