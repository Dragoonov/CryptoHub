package com.moonlightbutterfly.cryptohub.data.assets

import com.moonlightbutterfly.cryptohub.data.common.getOrThrow
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class CryptoAssetsDataSourceImplTest {

    private val generalMetadataDto = GeneralMetadataDto(
        mapOf(
            "test" to CryptoAssetMetadataDto(
                "testName", "sym", "logo", "desc"
            ),
            "test2" to CryptoAssetMetadataDto(
                "testName2", "sym2", "logo2", "desc2"
            ),
            "test3" to CryptoAssetMetadataDto(
                "testName3", "sym3", "logo3", "desc3"
            ),
            "test4" to CryptoAssetMetadataDto(
                "testName4", "sym4", "logo4", "desc4"
            )
        )
    )

    private val generalMarketQuoteDto = GeneralMarketQuoteDto(
        mapOf(
            "test" to CryptoAssetMarketQuoteDto(totalSupply = 1.0),
            "test2" to CryptoAssetMarketQuoteDto(totalSupply = 2.0),
            "test3" to CryptoAssetMarketQuoteDto(totalSupply = 3.0),
            "test4" to CryptoAssetMarketQuoteDto(totalSupply = 4.0),
        )
    )

    private val generalMarketListingDto = GeneralListingDto(
        generalMarketQuoteDto.data.values.toList()
    )

    private val coinMarketCapService: CoinMarketCapService = mockk {
        coEvery { getMetadata(any(), any()) } returns generalMetadataDto
        coEvery { getMarketInfo(any(), any()) } returns generalMarketQuoteDto
        coEvery { getListings(any(), any(), any()) } returns generalMarketListingDto
    }

    private val cryptoAssetsDataSourceImpl = CryptoAssetsDataSourceImpl(
        coinMarketCapService,
        mockk()
    )

    @Test
    fun `should return proper market info`() = runBlockingTest {
        // WHEN
        val list = cryptoAssetsDataSourceImpl.getCryptoAssetsMarketInfo(listOf("test")).first().getOrThrow()

        // THEN
        coVerify {
            coinMarketCapService.getMetadata(any(), any())
            coinMarketCapService.getMarketInfo(any(), any())
        }
        assertEquals(4, list.size)
        assertEquals(4, list.filter { it.maxSupply < 5 }.size)
        assertEquals(0, list.filter { it.maxSupply > 5 }.size)
    }

    @Test
    fun `should return proper market info by page`() = runBlockingTest {
        // WHEN
        val list = cryptoAssetsDataSourceImpl.getCryptoAssetsMarketInfo(1).first().getOrThrow()

        // THEN
        coVerify {
            coinMarketCapService.getListings(any(), any(), any())
            coinMarketCapService.getMetadata(any(), any())
        }
        assertEquals(4, list.size)
        assertEquals(4, list.filter { it.maxSupply < 5 }.size)
        assertEquals(0, list.filter { it.maxSupply > 5 }.size)
    }
}
