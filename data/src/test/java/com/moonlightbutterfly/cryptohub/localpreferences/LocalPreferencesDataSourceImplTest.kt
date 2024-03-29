package com.moonlightbutterfly.cryptohub.localpreferences

import com.moonlightbutterfly.cryptohub.data.common.getOrThrow
import com.moonlightbutterfly.cryptohub.database.daos.LocalPreferencesDao
import com.moonlightbutterfly.cryptohub.database.dtos.LocalPreferencesDto
import com.moonlightbutterfly.cryptohub.database.entities.LocalPreferencesEntity
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
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class LocalPreferencesDataSourceImplTest {

    private val flow: Flow<List<LocalPreferencesEntity>> = MutableStateFlow(
        listOf(
            LocalPreferencesEntity(1, LocalPreferencesDto(true, emptySet(), true))
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
    fun `should get local preferences`() = runTest {
        // WHEN
        val preferences = localPreferencesDataSourceImpl.getLocalPreferences()

        // THEN
        verify {
            localPreferencesDao.getAll()
        }
        assertTrue(preferences.first().getOrThrow().nightModeEnabled)
    }

    @Test
    fun `should update the local preferences`() = runTest {
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
