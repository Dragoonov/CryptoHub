package com.moonlightbutterfly.cryptohub.framework.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.moonlightbutterfly.cryptohub.framework.database.CryptoHubDatabase
import com.moonlightbutterfly.cryptohub.framework.database.entities.CryptoCollectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoCollectionsDao {

    @Query("SELECT name FROM ${CryptoHubDatabase.CRYPTO_COLLECTIONS_TABLE_NAME}")
    fun getAllCollectionNames(): Flow<List<String>>

    @Query("SELECT * FROM ${CryptoHubDatabase.CRYPTO_COLLECTIONS_TABLE_NAME} WHERE ${CryptoHubDatabase.CRYPTO_COLLECTIONS_NAME_COLUMN_NAME} = :name")
    fun getCollectionByName(name: String): Flow<List<CryptoCollectionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cryptoCollection: CryptoCollectionEntity)

    @Update
    suspend fun update(cryptoCollection: CryptoCollectionEntity)

    @Query("DELETE from ${CryptoHubDatabase.CRYPTO_COLLECTIONS_TABLE_NAME} WHERE ${CryptoHubDatabase.CRYPTO_COLLECTIONS_NAME_COLUMN_NAME} = :name")
    suspend fun remove(name: String)
}
