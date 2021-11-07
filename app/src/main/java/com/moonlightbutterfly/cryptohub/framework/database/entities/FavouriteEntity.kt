package com.moonlightbutterfly.cryptohub.framework.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.framework.database.CryptoHubDatabase

@Entity(tableName = CryptoHubDatabase.FAVOURITES_TABLE_NAME)
data class FavouriteEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = CryptoHubDatabase.FAVOURITES_ASSET_COLUMN_NAME)
    val asset: CryptoAsset,
)
