package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.localpreferences.LocalPreferencesRepository
import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class UpdateLocalPreferencesUseCaseTest {

    private val repositoryMock: LocalPreferencesRepository = mockk {
        coEvery { updateLocalPreferences(any()) } returns Result.Success(Unit)
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
