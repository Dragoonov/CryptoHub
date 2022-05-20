package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserCollectionsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class GetAllCollectionNamesUseCaseTest {

    private val names = flowOf(listOf("test", "test2"))
    private val userCollectionsRepository: UserCollectionsRepository = mockk {
        every { getAllCollectionNames() } returns names
    }

    private val useCase = GetAllCollectionNamesUseCase(userCollectionsRepository)

    @ExperimentalCoroutinesApi
    @Test
    fun `should get all names`() = runBlockingTest {
        // WHEN
        val result = useCase()

        // THEN
        verify {
            userCollectionsRepository.getAllCollectionNames()
        }
        assertEquals(names, result)
    }
}
