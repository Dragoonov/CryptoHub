package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.assets.CryptoAssetsRepository
import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class GetAllCryptoAssetsMarketInfoUseCaseTest {

    private val assets = flowOf(
        Result.Success(
            listOf(
                CryptoAssetMarketInfo(price = 12.1),
                CryptoAssetMarketInfo(price = 12.2),
                CryptoAssetMarketInfo(price = 12.3)
            )
        )
    )

    private val assets2 = flowOf(
        Result.Success(
            listOf(
                CryptoAssetMarketInfo(price = 10.1),
                CryptoAssetMarketInfo(price = 10.2),
            )
        )
    )

    private val repositoryMock: CryptoAssetsRepository = mockk {
        coEvery { getCryptoAssetsMarketInfo(eq(1)) } returns assets
        coEvery { getCryptoAssetsMarketInfo(eq(2)) } returns assets2
    }
    private val useCase = GetAllCryptoAssetsMarketInfoUseCase(repositoryMock)

    @ExperimentalCoroutinesApi
    @Test
    fun `should get assets`() = runBlockingTest {
        // GIVEN // WHEN
        val assetList = useCase(1)
        val assetList2 = useCase(2)
        // THEN
        coVerify {
            repositoryMock.getCryptoAssetsMarketInfo(1)
            repositoryMock.getCryptoAssetsMarketInfo(2)
        }
        assertEquals(assets, assetList)
        assertEquals(assets2, assetList2)
    }
}
