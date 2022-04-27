package com.moonlightbutterfly.cryptohub.framework.datasources

import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.framework.database.daos.FavouritesDao
import com.moonlightbutterfly.cryptohub.framework.database.daos.RecentsDao
import com.moonlightbutterfly.cryptohub.framework.database.entities.FavouriteEntity
import com.moonlightbutterfly.cryptohub.framework.database.entities.RecentEntity
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
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class UserConfigurationLocalDataSourceImplTest {

    private val favouritesFlow: Flow<List<FavouriteEntity>> = MutableStateFlow(
        listOf(
            FavouriteEntity(asset = CryptoAsset("Test1")),
            FavouriteEntity(asset = CryptoAsset("Test2")),
            FavouriteEntity(asset = CryptoAsset("Test3")),
        )
    )
    private val recentsFlow: Flow<List<RecentEntity>> = MutableStateFlow(
        listOf(
            RecentEntity(asset = CryptoAsset("Test4")),
            RecentEntity(asset = CryptoAsset("Test5")),
            RecentEntity(asset = CryptoAsset("Test6")),
        )
    )
    private val favouritesDao = mockk<FavouritesDao> {
        every { getAll() } returns favouritesFlow
        coEvery { insert(any()) } just Runs
        coEvery { remove(any()) } just Runs
    }

    private val recentsDao = mockk<RecentsDao> {
        every { getAll() } returns recentsFlow
        coEvery { insert(any()) } just Runs
        coEvery { removeAll() } just Runs
        coEvery { remove(any()) } just Runs
    }

    private val userConfigurationLocalDataSourceImpl =
        UserConfigurationLocalDataSourceImpl(favouritesDao, recentsDao)

    @Test
    fun `should get favourites`() = runBlockingTest {
        // WHEN
        val list = userConfigurationLocalDataSourceImpl.getFavourites().first()

        // THEN
        verify {
            favouritesDao.getAll()
        }
        assertEquals(3, list.size)
    }

    @Test
    fun `should get recents`() = runBlockingTest {
        // WHEN
        val list = userConfigurationLocalDataSourceImpl.getRecents().first()

        // THEN
        verify {
            recentsDao.getAll()
        }
        assertEquals(3, list.size)
    }

    @Test
    fun `should add favourite`() = runBlockingTest {
        // GIVEN
        val asset = CryptoAsset("name")

        // WHEN
        userConfigurationLocalDataSourceImpl.addFavourite(asset)

        // THEN
        coVerify {
            favouritesDao.insert(match { it.asset.name == "name" })
        }
    }

    @Test
    fun `should add recent`() = runBlockingTest {
        // GIVEN
        val asset = CryptoAsset("name")

        // WHEN
        userConfigurationLocalDataSourceImpl.addRecent(asset)

        // THEN
        coVerify {
            recentsDao.insert(match { it.asset.name == "name" })
        }
    }

    @Test
    fun `should remove favourite`() = runBlockingTest {
        // GIVEN
        val asset = CryptoAsset("name")

        // WHEN
        userConfigurationLocalDataSourceImpl.removeFavourite(asset)

        // THEN
        coVerify {
            favouritesDao.remove(match { it.name == "name" })
        }
    }

    @Test
    fun `should remove recent`() = runBlockingTest {
        // GIVEN
        val asset = CryptoAsset("name")

        // WHEN
        userConfigurationLocalDataSourceImpl.removeRecent(asset)

        // THEN
        coVerify {
            recentsDao.remove(match { it.name == "name" })
        }
    }

    @Test
    fun `should remove recents`() = runBlockingTest {
        // WHEN
        userConfigurationLocalDataSourceImpl.removeRecents()

        // THEN
        coVerify {
            recentsDao.removeAll()
        }
    }
}
