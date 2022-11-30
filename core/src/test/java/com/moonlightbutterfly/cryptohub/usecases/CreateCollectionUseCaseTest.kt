package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.collections.UserCollectionsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import com.moonlightbutterfly.cryptohub.data.common.Result

class CreateCollectionUseCaseTest {

    private val userCollectionsRepository: UserCollectionsRepository = mockk {
        coEvery { createCollection(any()) } returns Result.Success(Unit)
    }

    private val useCase = CreateCollectionUseCase(userCollectionsRepository)

    @ExperimentalCoroutinesApi
    @Test
    fun `should create collection`() = runBlockingTest {
        // WHEN
        useCase("")

        // THEN
        coVerify {
            userCollectionsRepository.createCollection("")
        }
    }
}
