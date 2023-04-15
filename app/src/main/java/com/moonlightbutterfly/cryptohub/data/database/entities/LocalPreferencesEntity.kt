package com.moonlightbutterfly.cryptohub.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moonlightbutterfly.cryptohub.data.database.CryptoHubDatabase
import com.moonlightbutterfly.cryptohub.data.database.dtos.LocalPreferencesDto

@Entity(tableName = CryptoHubDatabase.LOCAL_PREFERENCES_TABLE_NAME)
data class LocalPreferencesEntity(

    @PrimaryKey
    val id: Int = 1,

    @ColumnInfo(name = CryptoHubDatabase.LOCAL_PREFERENCES_COLUMN_NAME)
    val preferences: LocalPreferencesDto,
)
