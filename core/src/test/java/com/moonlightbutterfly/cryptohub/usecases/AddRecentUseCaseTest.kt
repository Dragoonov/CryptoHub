package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserConfigurationRepository
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class AddRecentUseCaseTest {

    private val repositoryMock: UserConfigurationRepository = mockk {
        coEvery { addRecent(any()) } just Runs
    }
    private val useCase = AddRecentUseCase(repositoryMock)

    @ExperimentalCoroutinesApi
    @Test
    fun `should add recent`() = runBlockingTest {
        // GIVEN
        val recent = CryptoAsset("test1", "ts1", "test1")
        // WHEN
        useCase(recent)
        // THEN
        coVerify(exactly = 1) {
            repositoryMock.addRecent(recent)
        }
    }
}
