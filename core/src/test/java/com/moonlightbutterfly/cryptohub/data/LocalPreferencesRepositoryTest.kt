package com.moonlightbutterfly.cryptohub.data

import com.moonlightbutterfly.cryptohub.domain.models.LocalPreferences
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class LocalPreferencesRepositoryTest {

    private val localPreferencesFlow = MutableStateFlow(LocalPreferences.DEFAULT)
    private val localPreferencesDataSource: LocalPreferencesDataSource = mockk {
        coEvery { updateLocalPreferences(any()) } just Runs
        every { getLocalPreferences() } returns localPreferencesFlow
    }
    private val repository = LocalPreferencesRepository(localPreferencesDataSource)

    @Test
    fun `should update local preferences`() = runBlockingTest {
        // GIVEN
        val preferences = LocalPreferences(true)
        // WHEN
        repository.updateLocalPreferences(preferences)

        // THEN
        coVerify {
            localPreferencesDataSource.updateLocalPreferences(preferences)
        }
    }

    @Test
    fun `should get local preferences`() = runBlockingTest {
        // GIVEN WHEN
        val preferences = repository.getLocalPreferences().first()

        // THEN
        verify {
            localPreferencesDataSource.getLocalPreferences()
        }
        assertEquals(LocalPreferences.DEFAULT, preferences)
    }
}
