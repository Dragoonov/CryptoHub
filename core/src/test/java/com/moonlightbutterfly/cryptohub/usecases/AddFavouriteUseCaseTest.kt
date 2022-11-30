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

class AddFavouriteUseCaseTest {

    private val addAssetToCollectionUseCase: AddAssetToCollectionUseCase = mockk()

    private val useCase = AddFavouriteUseCase(addAssetToCollectionUseCase)

    @Before
    fun setup() {
        coEvery { addAssetToCollectionUseCase(any(), any()) } returns Result.Success(Unit)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should add to collection`() = runBlockingTest {
        // GIVEN
        val asset = CryptoAsset("test1", "ts1", "test1")
        // WHEN
        useCase(asset)
        // THEN
        coVerify(exactly = 1) {
            addAssetToCollectionUseCase(asset, UserCollectionsRepository.FAVOURITES_COLLECTION_NAME)
        }
    }
}
