package com.moonlightbutterfly.cryptohub.data.assets

import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class CryptoAssetsRepositoryTest {

    private val requestedSymbols1 = listOf("BTC")
    private val requestedSymbols2 = listOf("ETH")

    private val assets = flowOf(
        Answer.Success(
            listOf(
                CryptoAssetMarketInfo(price = 12.1),
                CryptoAssetMarketInfo(price = 12.2),
                CryptoAssetMarketInfo(price = 12.3)
            )
        )
    )

    private val assets2 = flowOf(
        Answer.Success(
            listOf(
                CryptoAssetMarketInfo(price = 10.1),
                CryptoAssetMarketInfo(price = 10.2),
            )
        )
    )

    private val mockDataSource = mockk<CryptoAssetsDataSource> {
        coEvery { getCryptoAssetsMarketInfo(eq(1)) } returns assets
        coEvery { getCryptoAssetsMarketInfo(eq(2)) } returns assets2
        coEvery { getCryptoAssetsMarketInfo(eq(requestedSymbols1)) } returns assets
        coEvery { getCryptoAssetsMarketInfo(eq(requestedSymbols2)) } returns assets2
    }

    private val repository = CryptoAssetsRepository(mockDataSource)

    @ExperimentalCoroutinesApi
    @Test
    fun `should get assets per page`() = runBlockingTest {
        // GIVEN // WHEN
        val assetList = repository.getCryptoAssetsMarketInfo(1)
        val assetList2 = repository.getCryptoAssetsMarketInfo(2)
        // THEN
        TestCase.assertEquals(assets, assetList)
        TestCase.assertEquals(assets2, assetList2)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should get assets per symbols`() = runBlockingTest {
        // GIVEN // WHEN
        val assetList = repository.getCryptoAssetsMarketInfo(requestedSymbols1).first()
        val assetList2 = repository.getCryptoAssetsMarketInfo(requestedSymbols2).first()
        // THEN
        TestCase.assertEquals(assets.first(), assetList)
        TestCase.assertEquals(assets2.first(), assetList2)
    }
}
