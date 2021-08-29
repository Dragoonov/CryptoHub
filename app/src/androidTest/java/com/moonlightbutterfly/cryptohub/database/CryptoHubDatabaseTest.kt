package com.moonlightbutterfly.cryptohub.database

import android.content.ContentValues
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import database.AppConfig
import database.CryptoHubDatabase
import database.daos.AppConfigDao
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class CryptoHubDatabaseTest {
    private lateinit var appConfigDao: AppConfigDao
    private lateinit var db: CryptoHubDatabase
    private var appConfig: AppConfig? = null
    private val appConfigObserver = Observer<List<AppConfig>> {
        appConfig = it.firstOrNull()
    }
    private val defaultAppConfig = AppConfig(1)
    private val prepopulationCallback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            val values = ContentValues().apply {
                put("night_mode", false)
            }
            db.insert("app_config", OnConflictStrategy.REPLACE, values)
        }
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        createDb()
        appConfigDao.getAll().observeForever(appConfigObserver)
    }

    private fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, CryptoHubDatabase::class.java)
            .addCallback(prepopulationCallback)
            .build()
        appConfigDao = db.appConfigDao()
    }

    @After
    fun tearDown() {
        appConfigDao.getAll().removeObserver(appConfigObserver)
        closeDb()
    }

    private fun closeDb() = db.close()

    @Test
    fun shouldRetrieveDefaultAppConfig() = runBlockingTest {
        // THEN
        assertEquals(defaultAppConfig, appConfig)
    }

    @Test
    fun shouldUpdateAndRetrieveAppConfig() = runBlockingTest {
        // GIVEN
        val appConfigToUpdate = defaultAppConfig.copy(nightModeEnabled = true)
        // WHEN
        appConfigDao.update(appConfigToUpdate)
        // THEN
        assertEquals(appConfigToUpdate, appConfig)
    }
}
