package com.moonlightbutterfly.cryptohub.collections

import com.moonlightbutterfly.cryptohub.data.common.getOrThrow
import com.moonlightbutterfly.cryptohub.database.daos.CryptoCollectionsDao
import com.moonlightbutterfly.cryptohub.database.dtos.CryptoAssetDto
import com.moonlightbutterfly.cryptohub.database.entities.CryptoCollectionEntity
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
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
                        CryptoAssetDto("Test1", "", ""),
                        CryptoAssetDto("Test2", "", ""),
                        CryptoAssetDto("Test3", "", ""),
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
        coEvery { update(any()) } returns 1
        coEvery { insert(any()) } returns 1
        coEvery { remove(any()) } returns 1
    }

    private val userConfigurationLocalDataSourceImpl =
        UserCollectionsLocalDataSourceImpl(cryptoCollectionsDao, mockk())

    @Test
    fun `should get collection`() = runTest {
        // WHEN
        val list = userConfigurationLocalDataSourceImpl
            .getCollection("").first().getOrThrow().cryptoAssets

        // THEN
        verify {
            cryptoCollectionsDao.getCollectionByName("")
        }
        assertEquals(3, list.size)
    }

    @Test
    fun `should get collection names`() = runTest {
        // WHEN
        val list = userConfigurationLocalDataSourceImpl.getAllCollectionNames().first().getOrThrow()

        // THEN
        verify {
            cryptoCollectionsDao.getAllCollectionNames()
        }
        assertEquals(2, list.size)
    }

    @Test
    fun `should clear collection`() = runTest {
        // WHEN
        userConfigurationLocalDataSourceImpl.clearCollection("")

        // THEN
        coVerify {
            cryptoCollectionsDao.update(match { it.assets.isEmpty() && it.name == "" })
        }
    }

    @Test
    fun `should create collection`() = runTest {
        // WHEN
        userConfigurationLocalDataSourceImpl.createCollection("")

        // THEN
        coVerify {
            cryptoCollectionsDao.insert(match { it.assets.isEmpty() && it.name == "" })
        }
    }

    @Test
    fun `should remove collection`() = runTest {
        // WHEN
        userConfigurationLocalDataSourceImpl.removeCollection("")

        // THEN
        coVerify {
            cryptoCollectionsDao.remove("")
        }
    }

    @Test
    fun `should add to collection`() = runTest {
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
    fun `should create collection on adding to non existent collection`() = runTest {
        // GIVEN
        val asset = CryptoAsset("name")
        every {
            cryptoCollectionsDao.getCollectionByName(eq("test"))
        } returns flowOf(emptyList()) andThen collectionFlow

        // WHEN
        userConfigurationLocalDataSourceImpl.addToCollection(asset, "test")

        // THEN
        coVerify {
            cryptoCollectionsDao.insert(match { it.name == "test" && it.assets.isEmpty() })
            cryptoCollectionsDao.update(match { it.assets.size == 4 && it.name == "test" })
        }
    }

    @Test
    fun `should remove from collection`() = runTest {
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
