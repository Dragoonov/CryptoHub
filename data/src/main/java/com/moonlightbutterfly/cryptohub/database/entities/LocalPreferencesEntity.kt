package com.moonlightbutterfly.cryptohub.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moonlightbutterfly.cryptohub.database.CryptoHubDatabase
import com.moonlightbutterfly.cryptohub.database.dtos.LocalPreferencesDto

@Entity(tableName = CryptoHubDatabase.LOCAL_PREFERENCES_TABLE_NAME)
internal data class LocalPreferencesEntity(

    @PrimaryKey
    val id: Int = 1,

    @ColumnInfo(name = CryptoHubDatabase.LOCAL_PREFERENCES_COLUMN_NAME)
    val preferences: LocalPreferencesDto,
)
