package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.CryptoAssetsRepository
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAssetMarketInfo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class GetCryptoAssetsMarketInfoUseCaseTest {

    private val requestedSymbols1 = listOf("BTC")
    private val requestedSymbols2 = listOf("ETH")

    private val assets = listOf(
        CryptoAssetMarketInfo(price = 12.1),
        CryptoAssetMarketInfo(price = 12.2),
        CryptoAssetMarketInfo(price = 12.3)
    )

    private val assets2 = listOf(
        CryptoAssetMarketInfo(price = 10.1),
        CryptoAssetMarketInfo(price = 10.2),
    )

    private val repositoryMock: CryptoAssetsRepository = mockk {
        coEvery { getCryptoAssetsMarketInfo(eq(requestedSymbols1)) } returns assets
        coEvery { getCryptoAssetsMarketInfo(eq(requestedSymbols2)) } returns assets2
    }
    private val useCase = GetCryptoAssetsMarketInfoUseCase(repositoryMock)

    @ExperimentalCoroutinesApi
    @Test
    fun `should get assets`() = runBlockingTest {
        // GIVEN // WHEN
        val assetList = useCase(requestedSymbols1)
        val assetList2 = useCase(requestedSymbols2)
        // THEN
        coVerify {
            repositoryMock.getCryptoAssetsMarketInfo(requestedSymbols1)
            repositoryMock.getCryptoAssetsMarketInfo(requestedSymbols2)
        }
        assertEquals(assets, assetList)
        assertEquals(assets2, assetList2)
    }
}
