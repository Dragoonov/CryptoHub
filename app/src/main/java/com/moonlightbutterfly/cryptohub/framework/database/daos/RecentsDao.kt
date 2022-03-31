package com.moonlightbutterfly.cryptohub.framework.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.framework.database.CryptoHubDatabase
import com.moonlightbutterfly.cryptohub.framework.database.entities.RecentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentsDao {

    @Query("SELECT * FROM ${CryptoHubDatabase.RECENTS_TABLE_NAME}")
    fun getAll(): Flow<List<RecentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recent: RecentEntity)

    @Query("DELETE from ${CryptoHubDatabase.RECENTS_TABLE_NAME} WHERE ${CryptoHubDatabase.RECENTS_ASSET_COLUMN_NAME} = :asset")
    suspend fun remove(asset: CryptoAsset)

    @Query("DELETE from ${CryptoHubDatabase.RECENTS_TABLE_NAME}")
    suspend fun removeAll()
}
