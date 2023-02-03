package com.moonlightbutterfly.cryptohub.data.database

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
import com.moonlightbutterfly.cryptohub.data.database.converters.CryptoAssetConverter
import com.moonlightbutterfly.cryptohub.data.database.converters.LocalPreferencesConverter
import com.moonlightbutterfly.cryptohub.data.database.daos.CryptoCollectionsDao
import com.moonlightbutterfly.cryptohub.data.database.daos.LocalPreferencesDao
import com.moonlightbutterfly.cryptohub.data.database.entities.CryptoCollectionEntity
import com.moonlightbutterfly.cryptohub.data.database.entities.LocalPreferencesEntity
import com.moonlightbutterfly.cryptohub.models.LocalPreferences

@Database(entities = [LocalPreferencesEntity::class, CryptoCollectionEntity::class], version = 1)
@TypeConverters(LocalPreferencesConverter::class, CryptoAssetConverter::class)
abstract class CryptoHubDatabase : RoomDatabase() {

    abstract fun localPreferencesDao(): LocalPreferencesDao
    abstract fun cryptoCollectionsDao(): CryptoCollectionsDao

    companion object {
        private var INSTANCE: CryptoHubDatabase? = null
        private const val DATABASE_NAME = "crypto_hub_database"
        const val LOCAL_PREFERENCES_COLUMN_NAME = "preferences"
        const val CRYPTO_COLLECTIONS_TABLE_NAME = "crypto_collection"
        const val CRYPTO_COLLECTIONS_NAME_COLUMN_NAME = "name"
        const val CRYPTO_COLLECTIONS_ASSETS_COLUMN_NAME = "assets"
        const val LOCAL_PREFERENCES_TABLE_NAME = "local_preferences"
        // private const val DATABASE_TEMPLATE_PATH = "databases/$DATABASE_NAME.db"

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
            object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    val createLocalPreferences =
                        "CREATE TABLE IF NOT EXISTS $LOCAL_PREFERENCES_TABLE_NAME (" +
                            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                            "$LOCAL_PREFERENCES_COLUMN_NAME TEXT)"

                    val createCryptoCollections =
                        "CREATE TABLE IF NOT EXISTS $CRYPTO_COLLECTIONS_TABLE_NAME (" +
                            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                            "$CRYPTO_COLLECTIONS_NAME_COLUMN_NAME TEXT," +
                            "$CRYPTO_COLLECTIONS_ASSETS_COLUMN_NAME TEXT)"

                    val values = ContentValues().apply {
                        put(LOCAL_PREFERENCES_COLUMN_NAME, Gson().toJson(LocalPreferences.DEFAULT))
                    }

                    db.execSQL(createLocalPreferences)
                    db.execSQL(createCryptoCollections)
                    db.insert(LOCAL_PREFERENCES_TABLE_NAME, OnConflictStrategy.REPLACE, values)
                }
            }
    }
}
