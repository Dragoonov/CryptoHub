package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.LocalPreferencesRepository
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class UpdateLocalPreferencesUseCaseTest {

    private val repositoryMock: LocalPreferencesRepository = mockk {
        coEvery { updateLocalPreferences(any()) } just Runs
    }
    private val useCase = UpdateLocalPreferencesUseCase(repositoryMock)

    @ExperimentalCoroutinesApi
    @Test
    fun `should update local preferences`() = runBlockingTest {
        // GIVEN
        val preferences = LocalPreferences(true)
        // WHEN
        useCase(preferences)
        // THEN
        coVerify {
            repositoryMock.updateLocalPreferences(preferences)
        }
    }
}
