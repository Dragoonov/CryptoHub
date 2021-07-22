package database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_config")
data class AppConfig(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,

    @ColumnInfo(name = "night_mode")
    val nightModeEnabled: Boolean = false,
)