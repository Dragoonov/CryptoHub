package com.moonlightbutterfly.cryptohub.data.collections

import com.moonlightbutterfly.cryptohub.data.user.UserDataSource
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import com.moonlightbutterfly.cryptohub.models.User
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
import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.data.common.getOrThrow
import org.junit.Test

@ExperimentalCoroutinesApi
class UserCollectionsRepositoryTest {

    private val collectionLocal = flowOf(Result.Success(CryptoCollection(cryptoAssets = listOf(CryptoAsset.EMPTY, CryptoAsset.EMPTY))))
    private val collectionRemote = flowOf(Result.Success(CryptoCollection(cryptoAssets = listOf(CryptoAsset.EMPTY))))
    private val collectionNamesLocal = flowOf(Result.Success(listOf("local", "test2")))
    private val collectionNamesRemote = flowOf(Result.Success(listOf("remote", "test2")))

    private val userConfigurationLocalDataSource = mockk<UserCollectionsLocalDataSource> {
        every { getCollection(any()) } returns collectionLocal
        every { getAllCollectionNames() } returns collectionNamesLocal
        coEvery { createCollection(any()) } returns Result.Success(Unit)
        coEvery { addToCollection(any(), any()) } returns Result.Success(Unit)
        coEvery { removeCollection(any()) } returns Result.Success(Unit)
        coEvery { clearCollection(any()) } returns Result.Success(Unit)
        coEvery { removeFromCollection(any(), any()) } returns Result.Success(Unit)
    }

    private val userConfigurationRemoteDataSource = mockk<UserCollectionsRemoteDataSource> {
        every { getCollection(any(), any()) } returns collectionRemote
        every { getAllCollectionNames(any()) } returns collectionNamesRemote
        coEvery { createCollection(any(), any()) } returns Result.Success(Unit)
        coEvery { addToCollection(any(), any(), any()) } returns Result.Success(Unit)
        coEvery { removeCollection(any(), any()) } returns Result.Success(Unit)
        coEvery { clearCollection(any(), any()) } returns Result.Success(Unit)
        coEvery { removeFromCollection(any(), any(), any()) } returns Result.Success(Unit)
    }

    private val userDataSource = mockk<UserDataSource> {
        every { isUserSignedIn() } returns Result.Success(false) andThen Result.Success(true)
        every { getUser() } returns Result.Success(User("test"))
    }

    private val repository = UserCollectionsRepository(userConfigurationRemoteDataSource, userConfigurationLocalDataSource, userDataSource)

    @Test
    fun `should get collection`() = runBlockingTest {
        // GIVEN WHEN
        val local = repository.getCollection("").first()
        val remote = repository.getCollection("").first()

        // THEN
        verify {
            userConfigurationLocalDataSource.getCollection("")
            userConfigurationRemoteDataSource.getCollection("test", "")
        }
        assertEquals(collectionLocal.first().getOrThrow(), local.getOrThrow())
        assertEquals(collectionRemote.first().getOrThrow(), remote.getOrThrow())
    }

    @Test
    fun `should get collection names`() = runBlockingTest {
        // GIVEN WHEN
        val local = repository.getAllCollectionNames().first()
        val remote = repository.getAllCollectionNames().first()

        // THEN
        verify {
            userConfigurationLocalDataSource.getAllCollectionNames()
            userConfigurationRemoteDataSource.getAllCollectionNames("test")
        }
        assertEquals(collectionNamesLocal.first().getOrThrow(), local.getOrThrow())
        assertEquals(collectionNamesRemote.first().getOrThrow(), remote.getOrThrow())
    }

    @Test
    fun `should add to collection`() = runBlockingTest {
        // GIVEN
        val asset = CryptoAsset(name = "test")
        // WHEN
        repository.addToCollection(asset, "")
        repository.addToCollection(asset, "")

        // THEN
        coVerify {
            userConfigurationLocalDataSource.addToCollection(asset, "")
            userConfigurationRemoteDataSource.addToCollection("test", asset, "")
        }
    }

    @Test
    fun `should clear collection`() = runBlockingTest {
        // WHEN
        repository.clearCollection("")
        repository.clearCollection("")

        // THEN
        coVerify {
            userConfigurationLocalDataSource.clearCollection("")
            userConfigurationRemoteDataSource.clearCollection("test", "")
        }
    }

    @Test
    fun `should create collection`() = runBlockingTest {
        // WHEN
        repository.createCollection("")
        repository.createCollection("")

        // THEN
        coVerify {
            userConfigurationLocalDataSource.createCollection("")
            userConfigurationRemoteDataSource.createCollection("test", "")
        }
    }

    @Test
    fun `should remove collection`() = runBlockingTest {
        // WHEN
        repository.removeCollection("")
        repository.removeCollection("")

        // THEN
        coVerify {
            userConfigurationLocalDataSource.removeCollection("")
            userConfigurationRemoteDataSource.removeCollection("test", "")
        }
    }

    @Test
    fun `should remove from collection`() = runBlockingTest {
        // GIVEN
        val asset = CryptoAsset.EMPTY

        // WHEN
        repository.removeFromCollection(asset, "")
        repository.removeFromCollection(asset, "")

        // THEN
        coVerify {
            userConfigurationLocalDataSource.removeFromCollection(asset, "")
            userConfigurationRemoteDataSource.removeFromCollection("test", asset, "")
        }
    }
}
