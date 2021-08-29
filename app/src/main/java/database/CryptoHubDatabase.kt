package database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import database.daos.AppConfigDao

@Database(entities = [AppConfig::class], version = 1)
abstract class CryptoHubDatabase : RoomDatabase() {

    abstract fun appConfigDao(): AppConfigDao

    companion object {
        private var INSTANCE: CryptoHubDatabase? = null
        const val DATABASE_NAME = "app_config_database"

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
                .build()
        }
    }
}
