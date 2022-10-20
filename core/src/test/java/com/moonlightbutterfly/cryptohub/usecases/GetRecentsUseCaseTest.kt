package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserCollectionsRepository
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class GetRecentsUseCaseTest {

    private val item1 = CryptoAsset("test1", "ts1", "test1")
    private val item2 = CryptoAsset("test2", "ts2", "test2")

    private val collection = flowOf(CryptoCollection(cryptoAssets = listOf(item1, item2)))

    private val getCollectionUseCase: GetCollectionUseCase = mockk()

    private val useCase = GetRecentsUseCase(getCollectionUseCase)

    @Before
    fun setup() {
        every { getCollectionUseCase(any()) } returns collection
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should get collection`() = runBlockingTest {
        // GIVEN // WHEN
        val collectionList = useCase().first()
        // THEN
        verify {
            getCollectionUseCase(UserCollectionsRepository.RECENTS_COLLECTION_NAME)
        }
        assertEquals(collection.first(), collectionList)
    }
}
