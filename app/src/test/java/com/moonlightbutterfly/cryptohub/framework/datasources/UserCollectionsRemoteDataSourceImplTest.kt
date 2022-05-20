package com.moonlightbutterfly.cryptohub.framework.datasources

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
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

    private val task = mockk<Task<DocumentSnapshot>>(relaxed = true)
    private val documentReference = mockk<DocumentReference>(relaxed = true) {
        every { get() } returns task
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
        userId,
    )

    @ExperimentalCoroutinesApi
    @Test
    fun `should get collection names flow`() = runBlockingTest {
        // WHEN
        val result = userConfigurationRemoteDataSourceImpl.getAllCollectionNames()

        // THEN
        assertEquals(0, result.first().size)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should get collection`() = runBlockingTest {
        // WHEN
        val result = userConfigurationRemoteDataSourceImpl.getCollection("")

        // THEN
        assertEquals(0, result.first().cryptoAssets.size)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should clear collection`() = runBlockingTest {
        // WHEN
        userConfigurationRemoteDataSourceImpl.clearCollection("")

        // THEN
        verify {
            documentReference.update("", match { (it as List<CryptoAsset>).size == 0 }, *arrayOf())
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should create collection`() = runBlockingTest {
        // WHEN
        userConfigurationRemoteDataSourceImpl.createCollection("")

        // THEN
        verify {
            documentReference.update("", match { (it as List<CryptoAsset>).size == 0 }, *arrayOf())
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should remove collection`() = runBlockingTest {
        // WHEN
        userConfigurationRemoteDataSourceImpl.removeCollection("")

        // THEN
        verify {
            documentReference.update("", any(), *arrayOf())
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should add to collection`() = runBlockingTest {
        // WHEN
        userConfigurationRemoteDataSourceImpl.addToCollection(mockk(), "")

        // THEN
        verify {
            documentReference.update("", any(), *arrayOf())
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should remove from collection`() = runBlockingTest {
        // WHEN
        userConfigurationRemoteDataSourceImpl.removeFromCollection(mockk(), "")

        // THEN
        verify {
            documentReference.update("", any(), *arrayOf())
        }
    }
}
