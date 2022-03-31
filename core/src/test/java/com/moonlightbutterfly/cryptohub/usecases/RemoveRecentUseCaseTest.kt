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

class RemoveRecentUseCaseTest {

    private val repositoryMock: UserConfigurationRepository = mockk {
        coEvery { removeRecent(any()) } just Runs
    }
    private val useCase = RemoveRecentUseCase(repositoryMock)

    @ExperimentalCoroutinesApi
    @Test
    fun `should remove recent`() = runBlockingTest {
        // GIVEN
        val recent = CryptoAsset("test1", "ts1", "test1")
        // WHEN
        useCase(recent)
        // THEN
        coVerify {
            repositoryMock.removeRecent(recent)
        }
    }
}
