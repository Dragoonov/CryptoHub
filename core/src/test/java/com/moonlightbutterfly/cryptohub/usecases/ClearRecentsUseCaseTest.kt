package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.data.UserCollectionsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class ClearRecentsUseCaseTest {

    private val repositoryMock: UserCollectionsRepository = mockk {
        coEvery { clearCollection(any()) } returns Result.Success(Unit)
    }
    private val useCase = ClearRecentsUseCase(repositoryMock)

    @ExperimentalCoroutinesApi
    @Test
    fun `should remove recents`() = runBlockingTest {
        // GIVEN WHEN
        useCase()
        // THEN
        coVerify {
            repositoryMock.clearCollection(UserCollectionsRepository.RECENTS_COLLECTION_NAME)
        }
    }
}