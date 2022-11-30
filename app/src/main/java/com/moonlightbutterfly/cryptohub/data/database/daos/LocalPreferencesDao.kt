package com.moonlightbutterfly.cryptohub.data.database.daos

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.moonlightbutterfly.cryptohub.data.database.CryptoHubDatabase
import com.moonlightbutterfly.cryptohub.data.database.entities.LocalPreferencesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalPreferencesDao {

    @Query("SELECT * FROM ${CryptoHubDatabase.LOCAL_PREFERENCES_TABLE_NAME}")
    fun getAll(): Flow<List<LocalPreferencesEntity>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(config: LocalPreferencesEntity): Int
}
