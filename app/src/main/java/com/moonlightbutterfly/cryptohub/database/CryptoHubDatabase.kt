package com.moonlightbutterfly.cryptohub.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.moonlightbutterfly.cryptohub.database.daos.AppConfigDao
import com.moonlightbutterfly.cryptohub.database.daos.FavouritesDao
import com.moonlightbutterfly.cryptohub.dataobjects.AppConfig
import com.moonlightbutterfly.cryptohub.dataobjects.FavouriteCryptoasset

@Database(entities = [AppConfig::class, FavouriteCryptoasset::class], version = 1)
abstract class CryptoHubDatabase : RoomDatabase() {

    abstract fun appConfigDao(): AppConfigDao
    abstract fun favouritesDao(): FavouritesDao

    companion object {
        private var INSTANCE: CryptoHubDatabase? = null
        private const val DATABASE_NAME = "crypto_hub_database"

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
                .createFromAsset("databases/$DATABASE_NAME.db")
                    //TODO change after changing nomics
//                .addCallback(object : RoomDatabase.Callback() {
//                    override fun onCreate(db: SupportSQLiteDatabase) {
//                        val SQL_CREATE_APP_CONFIG =
//                            "CREATE TABLE IF NOT EXISTS app_config (" +
//                                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
//                                    "night_mode INTEGER)"
//
//                        val SQL_CREATE_FAVOURITES =
//                            "CREATE TABLE IF NOT EXISTS favourites (" +
//                                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
//                                    "symbol TEXT)"
//
//                        val values = ContentValues().apply {
//                            put("night_mode", false)
//                        }
//
//                        db.execSQL(SQL_CREATE_APP_CONFIG)
//                        db.execSQL(SQL_CREATE_FAVOURITES)
//                        db.insert("app_config", OnConflictStrategy.REPLACE, values)
//                    }
//                })
                .build()
        }
    }
}
