package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.collections.UserCollectionsRepository
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import com.moonlightbutterfly.cryptohub.data.common.Result

class AddAssetToCollectionUseCaseTest {

    private val userCollectionsRepository: UserCollectionsRepository = mockk {
        coEvery { addToCollection(any(), any()) } returns Result.Success(Unit)
    }

    private val useCase = AddAssetToCollectionUseCase(userCollectionsRepository)

    @ExperimentalCoroutinesApi
    @Test
    fun `should add asset to collection`() = runBlockingTest {
        // GIVEN
        val asset = CryptoAsset("test")
        // WHEN
        useCase(asset, "")

        // THEN
        coVerify {
            userCollectionsRepository.addToCollection(asset, "")
        }
    }
}
