package com.moonlightbutterfly.cryptohub.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moonlightbutterfly.cryptohub.database.CryptoHubDatabase
import com.moonlightbutterfly.cryptohub.database.dtos.CryptoAssetDto

@Entity(tableName = CryptoHubDatabase.CRYPTO_COLLECTIONS_TABLE_NAME)
internal data class CryptoCollectionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = CryptoHubDatabase.CRYPTO_COLLECTIONS_NAME_COLUMN_NAME)
    val name: String,

    @ColumnInfo(name = CryptoHubDatabase.CRYPTO_COLLECTIONS_ASSETS_COLUMN_NAME)
    val assets: List<CryptoAssetDto>
)
