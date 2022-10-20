package com.moonlightbutterfly.cryptohub.framework.datasources

import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.framework.database.daos.CryptoCollectionsDao
import com.moonlightbutterfly.cryptohub.framework.database.entities.CryptoCollectionEntity
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class UserCollectionsLocalDataSourceImplTest {

    private val collectionFlow: Flow<List<CryptoCollectionEntity>> =
        MutableStateFlow(
            listOf(
                CryptoCollectionEntity(
                    name = "test",
                    assets =
                    listOf(
                        CryptoAsset("Test1"),
                        CryptoAsset("Test2"),
                        CryptoAsset("Test3"),
                    )
                )
            )
        )

    private val namesFlow = flowOf(
        listOf(
            "test", "test2"
        )
    )

    private val cryptoCollectionsDao = mockk<CryptoCollectionsDao> {
        every { getCollectionByName(any()) } returns collectionFlow
        every { getAllCollectionNames() } returns namesFlow
        coEvery { update(any()) } just Runs
        coEvery { insert(any()) } just Runs
        coEvery { remove(any()) } just Runs
    }

    private val userConfigurationLocalDataSourceImpl =
        UserCollectionsLocalDataSourceImpl(cryptoCollectionsDao)

    @Test
    fun `should get collection`() = runBlockingTest {
        // WHEN
        val list = userConfigurationLocalDataSourceImpl
            .getCollection("").first().cryptoAssets

        // THEN
        verify {
            cryptoCollectionsDao.getCollectionByName("")
        }
        assertEquals(3, list.size)
    }

    @Test
    fun `should get collection names`() = runBlockingTest {
        // WHEN
        val list = userConfigurationLocalDataSourceImpl.getAllCollectionNames().first()

        // THEN
        verify {
            cryptoCollectionsDao.getAllCollectionNames()
        }
        assertEquals(2, list.size)
    }

    @Test
    fun `should clear collection`() = runBlockingTest {
        // WHEN
        userConfigurationLocalDataSourceImpl.clearCollection("")

        // THEN
        coVerify {
            cryptoCollectionsDao.update(match { it.assets.isEmpty() && it.name == "" })
        }
    }

    @Test
    fun `should create collection`() = runBlockingTest {
        // WHEN
        userConfigurationLocalDataSourceImpl.createCollection("")

        // THEN
        coVerify {
            cryptoCollectionsDao.insert(match { it.assets.isEmpty() && it.name == "" })
        }
    }

    @Test
    fun `should remove collection`() = runBlockingTest {
        // WHEN
        userConfigurationLocalDataSourceImpl.removeCollection("")

        // THEN
        coVerify {
            cryptoCollectionsDao.remove("")
        }
    }

    @Test
    fun `should add to collection`() = runBlockingTest {
        // GIVEN
        val asset = CryptoAsset("name")

        // WHEN
        userConfigurationLocalDataSourceImpl.addToCollection(asset, "test")

        // THEN
        coVerify {
            cryptoCollectionsDao.update(match { it.assets.size == 4 && it.name == "test" })
        }
    }

    @Test
    fun `should create collection on adding to non existent collection`() = runBlockingTest {
        // GIVEN
        val asset = CryptoAsset("name")
        every { cryptoCollectionsDao.getCollectionByName(eq("test")) } returns flowOf(emptyList()) andThen collectionFlow

        // WHEN
        userConfigurationLocalDataSourceImpl.addToCollection(asset, "test")

        // THEN
        coVerify {
            cryptoCollectionsDao.insert(match { it.name == "test" && it.assets.isEmpty() })
            cryptoCollectionsDao.update(match { it.assets.size == 4 && it.name == "test" })
        }
    }

    @Test
    fun `should remove from collection`() = runBlockingTest {
        // GIVEN
        val asset = CryptoAsset("Test2")

        // WHEN
        userConfigurationLocalDataSourceImpl.removeFromCollection(asset, "test")

        // THEN
        coVerify {
            cryptoCollectionsDao.update(match { it.assets.size == 2 && it.name == "test" })
        }
    }
}
