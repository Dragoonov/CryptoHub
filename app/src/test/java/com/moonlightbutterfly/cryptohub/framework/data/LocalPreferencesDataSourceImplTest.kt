package com.moonlightbutterfly.cryptohub.framework.data

import com.moonlightbutterfly.cryptohub.data.getOrThrow
import com.moonlightbutterfly.cryptohub.framework.database.daos.LocalPreferencesDao
import com.moonlightbutterfly.cryptohub.framework.database.entities.LocalPreferencesEntity
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class LocalPreferencesDataSourceImplTest {

    private val flow: Flow<List<LocalPreferencesEntity>> = MutableStateFlow(
        listOf(
            LocalPreferencesEntity(1, LocalPreferences(true))
        )
    )
    private val localPreferencesDao = mockk<LocalPreferencesDao> {
        every { getAll() } returns flow
        coEvery { update(any()) } returns 1
    }

    private val localPreferencesDataSourceImpl = LocalPreferencesDataSourceImpl(
        localPreferencesDao,
        mockk()
    )

    @Test
    fun `should get local preferences`() = runBlockingTest {
        // WHEN
        val preferences = localPreferencesDataSourceImpl.getLocalPreferences()

        // THEN
        verify {
            localPreferencesDao.getAll()
        }
        assertTrue(preferences.first().getOrThrow().nightModeEnabled)
    }

    @Test
    fun `should update the local preferences`() = runBlockingTest {
        // GIVEN
        val preferences = LocalPreferences(true)

        // WHEN
        localPreferencesDataSourceImpl.updateLocalPreferences(preferences)

        // THEN
        coVerify {
            localPreferencesDao.update(match { it.preferences.nightModeEnabled })
        }
    }
}
