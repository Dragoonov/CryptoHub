package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserCollectionsRepository
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class RemoveAssetFromCollectionUseCaseTest {

    private val userCollectionsRepository: UserCollectionsRepository = mockk {
        coEvery { removeFromCollection(any(), any()) } just Runs
    }

    private val useCase = RemoveAssetFromCollectionUseCase(userCollectionsRepository)

    @ExperimentalCoroutinesApi
    @Test
    fun `should remove asset from collection`() = runBlockingTest {
        // GIVEN
        val asset = CryptoAsset("test")
        // WHEN
        useCase(asset, "")

        // THEN
        coVerify {
            userCollectionsRepository.removeFromCollection(asset, "")
        }
    }
}
