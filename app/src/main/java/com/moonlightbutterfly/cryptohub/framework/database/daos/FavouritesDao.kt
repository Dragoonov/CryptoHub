package com.moonlightbutterfly.cryptohub.framework.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.framework.database.CryptoHubDatabase
import com.moonlightbutterfly.cryptohub.framework.database.entities.FavouriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritesDao {

    @Query("SELECT * FROM ${CryptoHubDatabase.FAVOURITES_TABLE_NAME}")
    fun getAll(): Flow<List<FavouriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favourite: FavouriteEntity)

    @Query("DELETE from ${CryptoHubDatabase.FAVOURITES_TABLE_NAME} WHERE ${CryptoHubDatabase.FAVOURITES_ASSET_COLUMN_NAME} = :asset")
    suspend fun remove(asset: CryptoAsset)
}
