package com.moonlightbutterfly.cryptohub.framework.datasources

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class UserConfigurationRemoteDataSourceImplTest {

    private val task = mockk<Task<DocumentSnapshot>>(relaxed = true)
    private val documentReference = mockk<DocumentReference>(relaxed = true) {
        every { get() } returns task
    }
    private val db = mockk<FirebaseFirestore>(relaxed = true) {
        every { collection(any()) } returns mockk(relaxed = true) {
            every { document(any()) } returns documentReference
        }
    }

    private val userConfigurationRemoteDataSourceImpl = UserConfigurationRemoteDataSourceImpl(db)

    @ExperimentalCoroutinesApi
    @Test
    fun `should register listener only once per userId`() = runBlockingTest {
        // GIVEN
        var userId = "1"

        // WHEN
        userConfigurationRemoteDataSourceImpl.registerFor(userId)
        userConfigurationRemoteDataSourceImpl.registerFor(userId)

        // GIVEN
        userId = "2"

        // WHEN
        userConfigurationRemoteDataSourceImpl.registerFor(userId)

        // THEN
        verify(exactly = 2) {
            task.addOnCompleteListener(any())
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should add favourite`() = runBlockingTest {
        // GIVEN
        val asset = CryptoAsset("name")

        // WHEN
        userConfigurationRemoteDataSourceImpl.addFavourite(asset)

        // THEN
        verify {
            documentReference.update(UserConfigurationRemoteDataSourceImpl.FAVOURITES_FIELD, any(), *arrayOf())
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should add recent`() = runBlockingTest {
        // GIVEN
        val asset = CryptoAsset("name")

        // WHEN
        userConfigurationRemoteDataSourceImpl.addRecent(asset)

        // THEN
        verify {
            documentReference.update(UserConfigurationRemoteDataSourceImpl.RECENTS_FIELD, any(), *arrayOf())
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should remove favourite`() = runBlockingTest {
        // GIVEN
        val asset = CryptoAsset("name")

        // WHEN
        userConfigurationRemoteDataSourceImpl.removeFavourite(asset)

        // THEN
        verify {
            documentReference.update(UserConfigurationRemoteDataSourceImpl.FAVOURITES_FIELD, any(), *arrayOf())
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should remove recent`() = runBlockingTest {
        // GIVEN
        val asset = CryptoAsset("name")

        // WHEN
        userConfigurationRemoteDataSourceImpl.removeRecent(asset)

        // THEN
        verify {
            documentReference.update(UserConfigurationRemoteDataSourceImpl.RECENTS_FIELD, any(), *arrayOf())
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should remove recents`() = runBlockingTest {
        // WHEN
        userConfigurationRemoteDataSourceImpl.removeRecents()

        // THEN
        verify {
            documentReference.update(UserConfigurationRemoteDataSourceImpl.RECENTS_FIELD, emptyList<CryptoAsset>())
        }
    }
}
