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

class AddFavouriteUseCaseTest {

    private val repositoryMock: UserConfigurationRepository = mockk {
        coEvery { addFavourite(any()) } just Runs
    }
    private val useCase = AddFavouriteUseCase(repositoryMock)

    @ExperimentalCoroutinesApi
    @Test
    fun `should add favourite`() = runBlockingTest {
        // GIVEN
        val favourite = CryptoAsset("test1", "ts1", "test1")
        // WHEN
        useCase(favourite)
        // THEN
        coVerify(exactly = 1) {
            repositoryMock.addFavourite(favourite)
        }
    }
}
