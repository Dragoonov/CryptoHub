package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.LocalPreferencesRepository
import com.moonlightbutterfly.cryptohub.domain.models.LocalPreferences
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class GetLocalPreferencesUseCaseTest {

    private val localPreferences = flowOf(LocalPreferences.DEFAULT)

    private val repositoryMock: LocalPreferencesRepository = mockk {
        every { getLocalPreferences() } returns localPreferences
    }

    private val useCase = GetLocalPreferencesUseCase(repositoryMock)

    @ExperimentalCoroutinesApi
    @Test
    fun `should return localPreferences`() = runBlockingTest {
        // WHEN
        val preferences = useCase().first()
        // THEN
        verify {
            repositoryMock.getLocalPreferences()
        }
        assertEquals(LocalPreferences.DEFAULT, preferences)
    }
}
