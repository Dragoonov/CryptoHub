package com.moonlightbutterfly.cryptohub.framework.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moonlightbutterfly.cryptohub.domain.models.UserSettings
import com.moonlightbutterfly.cryptohub.framework.database.CryptoHubDatabase

@Entity(tableName = CryptoHubDatabase.USER_SETTINGS_TABLE_NAME)
data class UserSettingsEntity(

    @PrimaryKey
    val id: Int = 1,

    @ColumnInfo(name = CryptoHubDatabase.USER_SETTINGS_SETTINGS_COLUMN_NAME)
    val settings: UserSettings,
)
