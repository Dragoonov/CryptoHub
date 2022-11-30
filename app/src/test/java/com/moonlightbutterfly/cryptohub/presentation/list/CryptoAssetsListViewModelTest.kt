package com.moonlightbutterfly.cryptohub.presentation.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import com.moonlightbutterfly.cryptohub.usecases.AddFavouriteUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetAllCryptoAssetsMarketInfoUseCase
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CryptoAssetsListViewModelTest {

    private val getAllCryptoAssetsMarketInfoUseCase: GetAllCryptoAssetsMarketInfoUseCase = mockk()
    private val getCryptoAssetsMarketInfoUseCase: GetCryptoAssetsMarketInfoUseCase = mockk()
    private val getFavouritesUseCase: GetFavouritesUseCase = mockk()
    private val addFavouriteUseCase: AddFavouriteUseCase = mockk()
    private val removeFavouriteUseCase: RemoveFavouriteUseCase = mockk()

    private lateinit var viewModel: CryptoAssetsListViewModel
    private val testDispatcher = TestCoroutineDispatcher()

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
        coEvery { getCryptoAssetsMarketInfoUseCase(any()) } returns flowOf(Result.Success(listOf()))

        viewModel = CryptoAssetsListViewModel(
            getAllCryptoAssetsMarketInfoUseCase,
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
    fun `should check if in favourites`() {
        // GIVEN
        val isInFavourites = CryptoAsset(symbol = "ETH")
        val isNotInFavourites = CryptoAsset(symbol = "DOT")

        // WHEN
        val check1 = viewModel.isCryptoInFavourites(isInFavourites)
        val check2 = viewModel.isCryptoInFavourites(isNotInFavourites)

        // THEN
        check1.observeForTesting {
            assertTrue(check1.value!!)
        }
        check2.observeForTesting {
            assertFalse(check2.value!!)
        }
    }

    @Test
    fun `should add to favourites`() {
        // GIVEN
        val asset = CryptoAsset.EMPTY

        // WHEN
        viewModel.addToFavourites(asset)

        // THEN
        coVerify {
            addFavouriteUseCase(asset)
        }
    }

    @Test
    fun `should remove from favourites`() {
        // GIVEN
        val asset = CryptoAsset.EMPTY

        // WHEN
        viewModel.removeFromFavourites(asset)

        // THEN
        coVerify {
            removeFavouriteUseCase(asset)
        }
    }
}
