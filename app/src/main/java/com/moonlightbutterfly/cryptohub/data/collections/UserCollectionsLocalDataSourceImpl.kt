package com.moonlightbutterfly.cryptohub.data.collections

import com.moonlightbutterfly.cryptohub.data.common.Error
import com.moonlightbutterfly.cryptohub.data.common.ErrorMapper
import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.data.database.daos.CryptoCollectionsDao
import com.moonlightbutterfly.cryptohub.data.database.entities.CryptoCollectionEntity
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Data source using the Room database.
 */
class UserCollectionsLocalDataSourceImpl(
    private val cryptoCollectionsDao: CryptoCollectionsDao,
    private val errorMapper: ErrorMapper,
) : UserCollectionsLocalDataSource {

    override fun getCollection(name: String): Flow<Result<CryptoCollection>> =
        cryptoCollectionsDao.getCollectionByName(name)
            .map { list ->
                list.map { CryptoCollection(it.name, it.assets) }
                    .firstOrNull()
                    .let {
                        if (it != null) {
                            Result.Success(it)
                        } else {
                            Result.Failure(Error.NotFound("Collection $name not found"))
                        }
                    }
            }
            .catch { emit(Result.Failure(errorMapper.mapError(it))) }

    override fun getAllCollectionNames(): Flow<Result<List<String>>> {
        return cryptoCollectionsDao
            .getAllCollectionNames()
            .map { Result.Success(it) as Result<List<String>> }
            .catch { emit(Result.Failure(errorMapper.mapError(it))) }
    }

    override suspend fun clearCollection(name: String): Result<Unit> {
        val collectionId = getCollectionEntity(name)?.id
        collectionId?.let {
            return cryptoCollectionsDao.update(
                CryptoCollectionEntity(
                    id = it,
                    name = name,
                    assets = emptyList()
                )
            ).getResult()
        } ?: return Result.Failure(Error.NotFound("Collection $name not found"))
    }

    private suspend fun getCollectionEntity(name: String): CryptoCollectionEntity? =
        cryptoCollectionsDao.getCollectionByName(name).first().firstOrNull()

    override suspend fun createCollection(name: String): Result<Unit> {
        return cryptoCollectionsDao
            .insert(CryptoCollectionEntity(name = name, assets = emptyList()))
            .getResult()
    }

    override suspend fun removeCollection(name: String): Result<Unit> {
        return cryptoCollectionsDao
            .remove(name)
            .getResult()
    }

    override suspend fun addToCollection(asset: CryptoAsset, collectionName: String): Result<Unit> {
        val collection = getCollectionEntity(collectionName)
        if (collection == null) {
            createCollection(collectionName)
        }
        return if (updateCollection(asset, collectionName, Collection<CryptoAsset>::plus) > 0) {
            Result.Success(Unit)
        } else {
            Result.Failure(Error.Unknown("Error updating collection"))
        }
    }

    override suspend fun removeFromCollection(asset: CryptoAsset, collectionName: String): Result<Unit> {
        return updateCollection(asset, collectionName, Collection<CryptoAsset>::minus).getResult()
    }

    private suspend fun updateCollection(
        cryptoAsset: CryptoAsset,
        collectionName: String,
        operation: Collection<CryptoAsset>.(CryptoAsset) -> List<CryptoAsset>
    ): Int {
        val collection = getCollectionEntity(collectionName) ?: return 0
        val newCollection = CryptoCollectionEntity(
            collection.id,
            collectionName,
            collection.assets.operation(cryptoAsset)
        )
        return cryptoCollectionsDao.update(newCollection)
    }

    private fun Int.getResult(): Result<Unit> {
        return if (this > 0) {
            Result.Success(Unit)
        } else {
            Result.Failure(Error.NotFound(""))
        }
    }

    private fun Long.getResult(): Result<Unit> {
        return if (this >= 0) {
            Result.Success(Unit)
        } else {
            Result.Failure(Error.Unknown(""))
        }
    }
}
