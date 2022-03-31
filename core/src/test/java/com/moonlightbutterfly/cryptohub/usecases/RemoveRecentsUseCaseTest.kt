package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserConfigurationRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class RemoveRecentsUseCaseTest {

    private val repositoryMock: UserConfigurationRepository = mockk {
        coEvery { removeRecents() } just Runs
    }
    private val useCase = RemoveRecentsUseCase(repositoryMock)

    @ExperimentalCoroutinesApi
    @Test
    fun `should remove recents`() = runBlockingTest {
        // GIVEN WHEN
        useCase()
        // THEN
        coVerify {
            repositoryMock.removeRecents()
        }
    }
}
