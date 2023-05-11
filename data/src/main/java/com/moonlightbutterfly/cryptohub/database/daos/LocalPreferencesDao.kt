package com.moonlightbutterfly.cryptohub.database.daos

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.moonlightbutterfly.cryptohub.database.CryptoHubDatabase
import com.moonlightbutterfly.cryptohub.database.entities.LocalPreferencesEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface LocalPreferencesDao {

    @Query("SELECT * FROM ${CryptoHubDatabase.LOCAL_PREFERENCES_TABLE_NAME}")
    fun getAll(): Flow<List<LocalPreferencesEntity>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(config: LocalPreferencesEntity): Int
}
