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
import com.moonlightbutterfly.cryptohub.domain.models.LocalPreferences
import com.moonlightbutterfly.cryptohub.framework.database.converters.CryptoAssetConverter
import com.moonlightbutterfly.cryptohub.framework.database.converters.LocalPreferencesConverter
import com.moonlightbutterfly.cryptohub.framework.database.daos.FavouritesDao
import com.moonlightbutterfly.cryptohub.framework.database.daos.LocalPreferencesDao
import com.moonlightbutterfly.cryptohub.framework.database.daos.RecentsDao
import com.moonlightbutterfly.cryptohub.framework.database.entities.FavouriteEntity
import com.moonlightbutterfly.cryptohub.framework.database.entities.LocalPreferencesEntity
import com.moonlightbutterfly.cryptohub.framework.database.entities.RecentEntity

@Database(entities = [LocalPreferencesEntity::class, FavouriteEntity::class, RecentEntity::class], version = 1)
@TypeConverters(LocalPreferencesConverter::class, CryptoAssetConverter::class)
abstract class CryptoHubDatabase : RoomDatabase() {

    abstract fun localPreferencesDao(): LocalPreferencesDao
    abstract fun favouritesDao(): FavouritesDao
    abstract fun recentsDao(): RecentsDao

    companion object {
        private var INSTANCE: CryptoHubDatabase? = null
        private const val DATABASE_NAME = "crypto_hub_database"
        private const val DATABASE_TEMPLATE_PATH = "databases/$DATABASE_NAME.db"
        const val LOCAL_PREFERENCES_TABLE_NAME = "local_preferences"
        const val FAVOURITES_TABLE_NAME = "favourites"
        const val RECENTS_TABLE_NAME = "recents"
        const val LOCAL_PREFERENCES_COLUMN_NAME = "preferences"
        const val FAVOURITES_ASSET_COLUMN_NAME = "asset"
        const val RECENTS_ASSET_COLUMN_NAME = "asset"

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
            )
                .fallbackToDestructiveMigration()
                // .createFromAsset(DATABASE_TEMPLATE_PATH)
                .addCallback(initializeCallbackHelper)
                .build()
        }

        private val initializeCallbackHelper =
            object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    val createLocalPreferences =
                        "CREATE TABLE IF NOT EXISTS $LOCAL_PREFERENCES_TABLE_NAME (" +
                            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                            "$LOCAL_PREFERENCES_COLUMN_NAME TEXT)"

                    val createFavourites =
                        "CREATE TABLE IF NOT EXISTS $FAVOURITES_TABLE_NAME (" +
                            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                            "$FAVOURITES_ASSET_COLUMN_NAME TEXT)"

                    val createRecents =
                        "CREATE TABLE IF NOT EXISTS $RECENTS_TABLE_NAME (" +
                            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                            "$RECENTS_ASSET_COLUMN_NAME TEXT)"

                    val values = ContentValues().apply {
                        put(LOCAL_PREFERENCES_COLUMN_NAME, Gson().toJson(LocalPreferences.DEFAULT))
                    }

                    db.execSQL(createLocalPreferences)
                    db.execSQL(createFavourites)
                    db.execSQL(createRecents)
                    db.insert(LOCAL_PREFERENCES_TABLE_NAME, OnConflictStrategy.REPLACE, values)
                }
            }
    }
}
