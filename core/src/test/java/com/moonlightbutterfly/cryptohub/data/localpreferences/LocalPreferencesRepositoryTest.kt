package com.moonlightbutterfly.cryptohub.data.localpreferences

import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.data.common.getOrThrow

@ExperimentalCoroutinesApi
class LocalPreferencesRepositoryTest {

    private val localPreferencesFlow = MutableStateFlow(Result.Success(LocalPreferences.DEFAULT))
    private val localPreferencesDataSource: LocalPreferencesDataSource = mockk {
        coEvery { updateLocalPreferences(any()) } returns Result.Success(Unit)
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
        assertEquals(LocalPreferences.DEFAULT, preferences.getOrThrow())
    }
}
