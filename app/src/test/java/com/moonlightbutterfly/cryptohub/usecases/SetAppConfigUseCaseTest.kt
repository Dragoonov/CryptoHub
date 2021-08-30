package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.repository.CryptoHubInternalRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class SetAppConfigUseCaseTest {

    private val repositoryMock: CryptoHubInternalRepository = mockk {
        coEvery { updateAppConfig(any()) } just Runs
    }

    private val useCase = SetAppConfigUseCase(repositoryMock)

    @ExperimentalCoroutinesApi
    @Test
    fun `should update appConfig`() = runBlockingTest {
        // WHEN
        useCase.updateAppConfig(mockk())
        // THEN
        coVerify(exactly = 1) {
            repositoryMock.updateAppConfig(any())
        }
    }
}
