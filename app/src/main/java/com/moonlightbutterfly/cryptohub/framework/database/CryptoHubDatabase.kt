package com.moonlightbutterfly.cryptohub.framework.database

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import androidx.room.Database
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.moonlightbutterfly.cryptohub.domain.models.UserSettings
import com.moonlightbutterfly.cryptohub.framework.database.converters.CryptoAssetConverter
import com.moonlightbutterfly.cryptohub.framework.database.converters.UserSettingsConverter
import com.moonlightbutterfly.cryptohub.framework.database.daos.FavouritesDao
import com.moonlightbutterfly.cryptohub.framework.database.daos.UserSettingsDao
import com.moonlightbutterfly.cryptohub.framework.database.entities.FavouriteEntity
import com.moonlightbutterfly.cryptohub.framework.database.entities.UserSettingsEntity

@Database(entities = [UserSettingsEntity::class, FavouriteEntity::class], version = 1)
@TypeConverters(UserSettingsConverter::class, CryptoAssetConverter::class)
abstract class CryptoHubDatabase : RoomDatabase() {

    abstract fun userSettingsDao(): UserSettingsDao
    abstract fun favouritesDao(): FavouritesDao

    companion object {
        private var INSTANCE: CryptoHubDatabase? = null
        private const val DATABASE_NAME = "crypto_hub_database"
        private const val DATABASE_TEMPLATE_PATH = "databases/$DATABASE_NAME.db"
        const val USER_SETTINGS_TABLE_NAME = "user_settings"
        const val FAVOURITES_TABLE_NAME = "favourites"
        const val USER_SETTINGS_SETTINGS_COLUMN_NAME = "settings"
        const val FAVOURITES_ASSET_COLUMN_NAME = "asset"

        fun getInstance(context: Context): CryptoHubDatabase {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = createDbInstance(context)
                }
                return INSTANCE as CryptoHubDatabase
            }
        }

        private fun createDbInstance(context: Context): CryptoHubDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                CryptoHubDatabase::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration()
                .createFromAsset(DATABASE_TEMPLATE_PATH)
                .build()
        }

        private val initializeCallbackHelper =
            object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    val createUserSettings =
                        "CREATE TABLE IF NOT EXISTS $USER_SETTINGS_TABLE_NAME (" +
                            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                            "$USER_SETTINGS_SETTINGS_COLUMN_NAME TEXT)"

                    val createFavourites =
                        "CREATE TABLE IF NOT EXISTS $FAVOURITES_TABLE_NAME (" +
                            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                            "$FAVOURITES_ASSET_COLUMN_NAME TEXT)"

                    val values = ContentValues().apply {
                        put(USER_SETTINGS_SETTINGS_COLUMN_NAME, Gson().toJson(UserSettings.EMPTY))
                    }

                    db.execSQL(createUserSettings)
                    db.execSQL(createFavourites)
                    db.insert(USER_SETTINGS_TABLE_NAME, OnConflictStrategy.REPLACE, values)
                }
            }
    }
}
