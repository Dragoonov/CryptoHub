package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserCollectionsRepository
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class GetCollectionUseCaseTest {

    private val list = flowOf(CryptoCollection(cryptoAssets = listOf(CryptoAsset("test"))))
    private val userCollectionsRepository: UserCollectionsRepository = mockk {
        every { getCollection(any()) } returns list
    }

    private val useCase = GetCollectionUseCase(userCollectionsRepository)

    @ExperimentalCoroutinesApi
    @Test
    fun `should get collection`() = runBlockingTest {
        // WHEN
        val result = useCase("")

        // THEN
        verify {
            userCollectionsRepository.getCollection("")
        }
        assertEquals(list, result)
    }
}
