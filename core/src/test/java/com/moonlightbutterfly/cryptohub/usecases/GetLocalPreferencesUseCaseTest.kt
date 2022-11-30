package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.localpreferences.LocalPreferencesRepository
import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.data.common.getOrThrow
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
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

    private val localPreferences = flowOf(Result.Success(LocalPreferences.DEFAULT))

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
        assertEquals(LocalPreferences.DEFAULT, preferences.getOrThrow())
    }
}
