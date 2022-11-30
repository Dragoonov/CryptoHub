package com.moonlightbutterfly.cryptohub.data.collections

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.moonlightbutterfly.cryptohub.data.common.getOrThrow
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class UserCollectionsRemoteDataSourceImplTest {

    private val task = mockk<Task<DocumentSnapshot>>(relaxed = true) {
        every { isComplete } returns true
        every { exception } returns null
        every { result } returns mockk()
        every { isSuccessful } returns true
    }
    private val updateTask = mockk<Task<Void>>(relaxed = true) {
        every { isComplete } returns true
        every { exception } returns null
        every { result } returns mockk()
        every { isSuccessful } returns true
    }

    private val documentReference = mockk<DocumentReference>(relaxed = true) {
        every { get() } returns task
        every { update(any<String>(), any()) } returns updateTask
        every { addSnapshotListener(any()) } returns mockk()
    }
    private val db = mockk<FirebaseFirestore>(relaxed = true) {
        every { collection(any()) } returns mockk(relaxed = true) {
            every { document(any()) } returns documentReference
        }
    }
    private val userId = "1"

    private val userConfigurationRemoteDataSourceImpl = UserCollectionsRemoteDataSourceImpl(
        db,
        mockk()
    )

    @ExperimentalCoroutinesApi
    @Test
    fun `should get collection names flow`() = runBlockingTest {
        // WHEN
        val result = userConfigurationRemoteDataSourceImpl.getAllCollectionNames(userId)

        // THEN
        assertEquals(0, result.first().getOrThrow().size)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should get collection`() = runBlockingTest {
        // WHEN
        val result = userConfigurationRemoteDataSourceImpl.getCollection(userId, "")

        // THEN
        assertEquals(0, result.first().getOrThrow().cryptoAssets.size)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should clear collection`() = runBlockingTest {
        // WHEN
        userConfigurationRemoteDataSourceImpl.clearCollection(userId, "")

        // THEN
        verify {
            documentReference.update("", match { (it as List<CryptoAsset>).size == 0 }, *arrayOf())
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should create collection`() = runBlockingTest {
        // WHEN
        userConfigurationRemoteDataSourceImpl.createCollection(userId, "")

        // THEN
        verify {
            documentReference.update("", match { (it as List<CryptoAsset>).size == 0 }, *arrayOf())
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should remove collection`() = runBlockingTest {
        // WHEN
        userConfigurationRemoteDataSourceImpl.removeCollection(userId, "")

        // THEN
        verify {
            documentReference.update("", any(), *arrayOf())
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should add to collection`() = runBlockingTest {
        // WHEN
        userConfigurationRemoteDataSourceImpl.addToCollection(userId, mockk(), "")

        // THEN
        verify {
            documentReference.update("", any(), *arrayOf())
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should remove from collection`() = runBlockingTest {
        // WHEN
        userConfigurationRemoteDataSourceImpl.removeFromCollection(userId, mockk(), "")

        // THEN
        verify {
            documentReference.update("", any(), *arrayOf())
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should initialize for user only once`() = runBlockingTest {
        // WHEN
        userConfigurationRemoteDataSourceImpl.removeFromCollection(userId, mockk(), "")
        userConfigurationRemoteDataSourceImpl.removeFromCollection(userId, mockk(), "")

        // THEN
        verify(exactly = 1) {
            db.collection(any())
        }
    }
}
