package com.moonlightbutterfly.cryptohub.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moonlightbutterfly.cryptohub.data.database.CryptoHubDatabase
import com.moonlightbutterfly.cryptohub.models.CryptoAsset

@Entity(tableName = CryptoHubDatabase.CRYPTO_COLLECTIONS_TABLE_NAME)
data class CryptoCollectionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = CryptoHubDatabase.CRYPTO_COLLECTIONS_NAME_COLUMN_NAME)
    val name: String,

    @ColumnInfo(name = CryptoHubDatabase.CRYPTO_COLLECTIONS_ASSETS_COLUMN_NAME)
    val assets: List<CryptoAsset>
)
