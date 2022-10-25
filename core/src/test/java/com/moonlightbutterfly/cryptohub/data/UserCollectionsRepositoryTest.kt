package com.moonlightbutterfly.cryptohub.data

import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class UserCollectionsRepositoryTest {

    private val collection = flowOf(Result.Success(CryptoCollection(cryptoAssets = listOf(CryptoAsset.EMPTY, CryptoAsset.EMPTY))))
    private val collectionNames = flowOf(Result.Success(listOf("Test", "test2")))

    private val userConfigurationDataSource = mockk<UserCollectionsDataSource> {
        every { getCollection(any()) } returns collection
        every { getAllCollectionNames() } returns collectionNames
        coEvery { createCollection(any()) } returns Result.Success(Unit)
        coEvery { addToCollection(any(), any()) } returns Result.Success(Unit)
        coEvery { removeCollection(any()) } returns Result.Success(Unit)
        coEvery { clearCollection(any()) } returns Result.Success(Unit)
        coEvery { removeFromCollection(any(), any()) } returns Result.Success(Unit)
    }

    private val repository = UserCollectionsRepository(userConfigurationDataSource)

    @Test
    fun `should get collection`() = runBlockingTest {
        // GIVEN WHEN
        val collectionTest = repository.getCollection("").first()

        // THEN
        verify {
            userConfigurationDataSource.getCollection("")
        }
        assertEquals(collection.first(), collectionTest)
    }

    @Test
    fun `should get collection names`() = runBlockingTest {
        // GIVEN WHEN
        val names = repository.getAllCollectionNames().first()

        // THEN
        verify {
            userConfigurationDataSource.getAllCollectionNames()
        }
        assertEquals(collectionNames.first(), names)
    }

    @Test
    fun `should add to collection`() = runBlockingTest {
        // GIVEN
        val asset = CryptoAsset(name = "test")
        // WHEN
        repository.addToCollection(asset, "")

        // THEN
        coVerify {
            userConfigurationDataSource.addToCollection(asset, "")
        }
    }

    @Test
    fun `should clear collection`() = runBlockingTest {
        // WHEN
        repository.clearCollection("")

        // THEN
        coVerify {
            userConfigurationDataSource.clearCollection("")
        }
    }

    @Test
    fun `should create collection`() = runBlockingTest {
        // WHEN
        repository.createCollection("")

        // THEN
        coVerify {
            userConfigurationDataSource.createCollection("")
        }
    }

    @Test
    fun `should remove collection`() = runBlockingTest {
        // WHEN
        repository.removeCollection("")

        // THEN
        coVerify {
            userConfigurationDataSource.removeCollection("")
        }
    }

    @Test
    fun `should remove from collection`() = runBlockingTest {
        // GIVEN
        val asset = CryptoAsset.EMPTY

        // WHEN
        repository.removeFromCollection(asset, "")

        // THEN
        coVerify {
            userConfigurationDataSource.removeFromCollection(asset, "")
        }
    }
}
