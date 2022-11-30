package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.collections.UserCollectionsRepository
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import com.moonlightbutterfly.cryptohub.data.common.Result

class RemoveFavouriteUseCaseTest {

    private val removeAssetFromCollectionUseCase: RemoveAssetFromCollectionUseCase = mockk()

    private val useCase = RemoveFavouriteUseCase(removeAssetFromCollectionUseCase)

    @Before
    fun setup() {
        coEvery { removeAssetFromCollectionUseCase(any(), any()) } returns Result.Success(Unit)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should remove collection`() = runBlockingTest {
        // GIVEN
        val asset = CryptoAsset("test1", "ts1", "test1")
        // WHEN
        useCase(asset)
        // THEN
        coVerify {
            removeAssetFromCollectionUseCase(asset, UserCollectionsRepository.FAVOURITES_COLLECTION_NAME)
        }
    }
}
