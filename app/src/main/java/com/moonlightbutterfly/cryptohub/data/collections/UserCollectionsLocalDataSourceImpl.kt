package com.moonlightbutterfly.cryptohub.data.collections

import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.data.common.Error
import com.moonlightbutterfly.cryptohub.data.common.ErrorMapper
import com.moonlightbutterfly.cryptohub.data.database.daos.CryptoCollectionsDao
import com.moonlightbutterfly.cryptohub.data.database.dtos.toCryptoAsset
import com.moonlightbutterfly.cryptohub.data.database.dtos.toCryptoAssetDto
import com.moonlightbutterfly.cryptohub.data.database.entities.CryptoCollectionEntity
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Data source using the Room database.
 */
class UserCollectionsLocalDataSourceImpl @Inject constructor(
    private val cryptoCollectionsDao: CryptoCollectionsDao,
    private val errorMapper: ErrorMapper,
) : UserCollectionsLocalDataSource {

    override fun getCollection(name: String): Flow<Answer<CryptoCollection>> =
        cryptoCollectionsDao.getCollectionByName(name)
            .map { list ->
                list.map { collection ->
                    CryptoCollection(
                        collection.name,
                        collection.assets.map { it.toCryptoAsset() }
                    )
                }
                    .firstOrNull()
                    .let {
                        if (it != null) {
                            Answer.Success(it)
                        } else {
                            Answer.Failure(Error.NotFound("Collection $name not found"))
                        }
                    }
            }
            .catch { emit(Answer.Failure(errorMapper.mapError(it))) }

    override fun getAllCollectionNames(): Flow<Answer<List<String>>> {
        return cryptoCollectionsDao
            .getAllCollectionNames()
            .map { Answer.Success(it) as Answer<List<String>> }
            .catch { emit(Answer.Failure(errorMapper.mapError(it))) }
    }

    override suspend fun clearCollection(name: String): Answer<Unit> {
        val collectionId = getCollectionEntity(name)?.id
        collectionId?.let {
            return cryptoCollectionsDao.update(
                CryptoCollectionEntity(
                    id = it,
                    name = name,
                    assets = emptyList()
                )
            ).getResult()
        } ?: return Answer.Failure(Error.NotFound("Collection $name not found"))
    }

    private suspend fun getCollectionEntity(name: String): CryptoCollectionEntity? =
        cryptoCollectionsDao.getCollectionByName(name).first().firstOrNull()

    override suspend fun createCollection(name: String): Answer<Unit> {
        return cryptoCollectionsDao
            .insert(CryptoCollectionEntity(name = name, assets = emptyList()))
            .getResult()
    }

    override suspend fun removeCollection(name: String): Answer<Unit> {
        return cryptoCollectionsDao
            .remove(name)
            .getResult()
    }

    override suspend fun addToCollection(asset: CryptoAsset, collectionName: String): Answer<Unit> {
        val collection = getCollectionEntity(collectionName)
        if (collection == null) {
            createCollection(collectionName)
        }
        return if (updateCollection(asset, collectionName, Collection<CryptoAsset>::plus) > 0) {
            Answer.Success(Unit)
        } else {
            Answer.Failure(Error.Unknown("Error updating collection"))
        }
    }

    override suspend fun removeFromCollection(
        asset: CryptoAsset,
        collectionName: String
    ): Answer<Unit> {
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
            collection.assets.map { it.toCryptoAsset() }.operation(cryptoAsset)
                .map { it.toCryptoAssetDto() }
        )
        return cryptoCollectionsDao.update(newCollection)
    }

    private companion object {
        private fun Int.getResult(): Answer<Unit> {
            return if (this > 0) {
                Answer.Success(Unit)
            } else {
                Answer.Failure(Error.NotFound(""))
            }
        }

        private fun Long.getResult(): Answer<Unit> {
            return if (this >= 0) {
                Answer.Success(Unit)
            } else {
                Answer.Failure(Error.Unknown(""))
            }
        }
    }
}
