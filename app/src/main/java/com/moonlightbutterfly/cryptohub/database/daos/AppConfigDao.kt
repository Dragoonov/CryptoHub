package com.moonlightbutterfly.cryptohub.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.moonlightbutterfly.cryptohub.dataobjects.AppConfig

@Dao
interface AppConfigDao {

    @Query("SELECT * FROM app_config")
    fun getAll(): LiveData<List<AppConfig>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(config: AppConfig)
}
