package com.moonlightbutterfly.cryptohub.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moonlightbutterfly.cryptohub.dataobjects.FavouriteCryptoasset

@Dao
interface FavouritesDao {

    @Query("SELECT * FROM favourites")
    fun getAll(): LiveData<List<FavouriteCryptoasset>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favourite: FavouriteCryptoasset)

    @Query("DELETE from favourites WHERE symbol = :symbol")
    suspend fun remove(symbol: String)
}