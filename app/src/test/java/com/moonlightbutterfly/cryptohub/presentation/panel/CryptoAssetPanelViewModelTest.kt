package com.moonlightbutterfly.cryptohub.presentation.panel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import com.moonlightbutterfly.cryptohub.usecases.AddFavouriteUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetFavouritesUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveFavouriteUseCase
import com.moonlightbutterfly.cryptohub.utils.observeForTesting
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CryptoAssetPanelViewModelTest {

    private val getCryptoAssetsMarketInfoUseCase: GetCryptoAssetsMarketInfoUseCase = mockk()
    private val getFavouritesUseCase: GetFavouritesUseCase = mockk()
    private val addFavouriteUseCase: AddFavouriteUseCase = mockk()
    private val removeFavouriteUseCase: RemoveFavouriteUseCase = mockk()

    private lateinit var viewModel: CryptoAssetPanelViewModel
    private val testDispatcher = TestCoroutineDispatcher()

    private val asset = CryptoAsset(symbol = "DOT")
    private val asset2 = CryptoAsset(symbol = "ADA")

    private val marketAsset = CryptoAssetMarketInfo(asset = asset)
    private val marketAsset2 = CryptoAssetMarketInfo(asset = asset2)

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { getFavouritesUseCase() } returns flowOf(
            Result.Success(
                CryptoCollection(
                    cryptoAssets =
                    listOf(
                        CryptoAsset(symbol = "BTC"),
                        CryptoAsset(symbol = "ETH"),
                        CryptoAsset(symbol = "XRP"),
                        CryptoAsset(symbol = "ADA")
                    )
                )
            )
        )
        coEvery { getCryptoAssetsMarketInfoUseCase(any()) } returns flowOf(Result.Success(listOf(marketAsset)))

        viewModel = CryptoAssetPanelViewModel(
            getCryptoAssetsMarketInfoUseCase,
            getFavouritesUseCase,
            addFavouriteUseCase,
            removeFavouriteUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should be in favourites`() {
        // GIVEN
        coEvery { getCryptoAssetsMarketInfoUseCase(any()) } returns flowOf(Result.Success(listOf(marketAsset2)))
        val assetLiveData2 = viewModel.getCryptoAssetMarketInfo(asset2.symbol)
        assetLiveData2.observeForTesting {
            runBlockingTest {
                // WHEN
                val check1 = viewModel.isCryptoInFavourites().first()
                // THEN
                assertTrue(check1)
            }
        }
    }

    @Test
    fun `should not be in favourites`() {
        // GIVEN
        coEvery { getCryptoAssetsMarketInfoUseCase(any()) } returns flowOf(Result.Success(listOf(marketAsset)))
        val assetLiveData = viewModel.getCryptoAssetMarketInfo(asset.symbol)
        assetLiveData.observeForTesting {
            runBlockingTest {
                // WHEN
                val check1 = viewModel.isCryptoInFavourites().first()
                // THEN
                assertFalse(check1)
            }
        }
    }

    @Test
    fun `should add to favourites`() {
        // GIVEN
        val assetLiveData = viewModel.getCryptoAssetMarketInfo(asset.symbol)
        assetLiveData.observeForTesting {
            // WHEN
            viewModel.addCryptoToFavourites()

            // THEN
            coVerify {
                addFavouriteUseCase(asset)
            }
        }
    }

    @Test
    fun `should remove from favourites`() {
        // GIVEN
        val assetLiveData = viewModel.getCryptoAssetMarketInfo(asset.symbol)
        assetLiveData.observeForTesting {
            // WHEN
            viewModel.removeCryptoFromFavourites()

            // THEN
            coVerify {
                removeFavouriteUseCase(asset)
            }
        }
    }
}
