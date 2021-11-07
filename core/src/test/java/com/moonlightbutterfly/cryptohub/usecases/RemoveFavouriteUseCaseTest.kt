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

class RemoveFavouriteUseCaseTest {

    private val repositoryMock: UserConfigurationRepository = mockk {
        coEvery { removeFavourite(any()) } just Runs
    }
    private val useCase = RemoveFavouriteUseCase(repositoryMock)

    @ExperimentalCoroutinesApi
    @Test
    fun `should remove favourite`() = runBlockingTest {
        // GIVEN
        val favourite = CryptoAsset("test1", "ts1", "test1")
        // WHEN
        useCase(favourite)
        // THEN
        coVerify {
            repositoryMock.removeFavourite(favourite)
        }
    }
}
