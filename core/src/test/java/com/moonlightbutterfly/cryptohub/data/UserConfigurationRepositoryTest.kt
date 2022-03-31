package com.moonlightbutterfly.cryptohub.data

import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.domain.models.UserSettings
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class UserConfigurationRepositoryTest {

    private val favourites = flowOf(listOf(CryptoAsset.EMPTY, CryptoAsset.EMPTY))
    private val recents = flowOf(listOf(CryptoAsset.EMPTY, CryptoAsset.EMPTY))
    private val userSettings = flowOf(UserSettings.EMPTY)

    private val userConfigurationDataSource = mockk<UserConfigurationDataSource> {
        every { getFavourites() } returns favourites
        every { getUserSettings() } returns userSettings
        every { getRecents() } returns recents
        coEvery { addFavourite(any()) } just Runs
        coEvery { removeFavourite(any()) } just Runs
        coEvery { addRecent(any()) } just Runs
        coEvery { removeRecent(any()) } just Runs
        coEvery { removeRecents() } just Runs
        coEvery { updateUserSettings(any()) } just Runs
    }

    private val repository = UserConfigurationRepository(userConfigurationDataSource)

    @Test
    fun `should get favourites`() = runBlockingTest {
        // GIVEN WHEN
        val favouriteList = repository.getFavourites().first()

        // THEN
        verify {
            userConfigurationDataSource.getFavourites()
        }
        assertEquals(listOf(CryptoAsset.EMPTY, CryptoAsset.EMPTY), favouriteList)
    }

    @Test
    fun `should get recents`() = runBlockingTest {
        // GIVEN WHEN
        val recentList = repository.getRecents().first()

        // THEN
        verify {
            userConfigurationDataSource.getFavourites()
        }
        assertEquals(listOf(CryptoAsset.EMPTY, CryptoAsset.EMPTY), recentList)
    }

    @Test
    fun `should add favourite`() = runBlockingTest {
        // GIVEN
        val favourite = CryptoAsset(name = "test")
        // WHEN
        repository.addFavourite(favourite)

        // THEN
        coVerify {
            userConfigurationDataSource.addFavourite(favourite)
        }
    }

    @Test
    fun `should add recent`() = runBlockingTest {
        // GIVEN
        val recent = CryptoAsset(name = "test")
        // WHEN
        repository.addRecent(recent)

        // THEN
        coVerify {
            userConfigurationDataSource.addRecent(recent)
        }
    }

    @Test
    fun `should remove favourite`() = runBlockingTest {
        // GIVEN
        val favourite = CryptoAsset(name = "test")
        // WHEN
        repository.removeFavourite(favourite)

        // THEN
        coVerify {
            userConfigurationDataSource.removeFavourite(favourite)
        }
    }

    @Test
    fun `should remove recent`() = runBlockingTest {
        // GIVEN
        val recent = CryptoAsset(name = "test")
        // WHEN
        repository.removeRecent(recent)

        // THEN
        coVerify {
            userConfigurationDataSource.removeRecent(recent)
        }
    }

    @Test
    fun `should remove recents`() = runBlockingTest {
        // GIVEN WHEN
        repository.removeRecents()

        // THEN
        coVerify {
            userConfigurationDataSource.removeRecents()
        }
    }

    @Test
    fun `should update user settings`() = runBlockingTest {
        // GIVEN
        val settings = UserSettings(true)
        // WHEN
        repository.updateUserSettings(settings)

        // THEN
        coVerify {
            userConfigurationDataSource.updateUserSettings(settings)
        }
    }

    @Test
    fun `should get user settings`() = runBlockingTest {
        // GIVEN WHEN
        val settings = repository.getUserSettings().first()

        // THEN
        verify {
            userConfigurationDataSource.getUserSettings()
        }
        assertEquals(UserSettings.EMPTY, settings)
    }
}
