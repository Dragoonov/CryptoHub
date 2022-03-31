package com.moonlightbutterfly.cryptohub.framework.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.framework.database.CryptoHubDatabase

@Entity(tableName = CryptoHubDatabase.RECENTS_TABLE_NAME)
data class RecentEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = CryptoHubDatabase.RECENTS_ASSET_COLUMN_NAME)
    val asset: CryptoAsset,
)
