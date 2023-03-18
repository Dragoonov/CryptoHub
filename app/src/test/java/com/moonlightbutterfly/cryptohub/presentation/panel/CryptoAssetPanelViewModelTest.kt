package com.moonlightbutterfly.cryptohub.presentation.panel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import com.moonlightbutterfly.cryptohub.models.NotificationConfiguration
import com.moonlightbutterfly.cryptohub.usecases.AddFavouriteUseCase
import com.moonlightbutterfly.cryptohub.usecases.ConfigureNotificationsUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetFavouritesUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveFavouriteUseCase
import com.moonlightbutterfly.cryptohub.usecases.UpdateLocalPreferencesUseCase
import com.moonlightbutterfly.cryptohub.utils.observeForTesting
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
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
    private val getLocalPreferencesUseCase: GetLocalPreferencesUseCase = mockk()
    private val updateLocalPreferencesUseCase: UpdateLocalPreferencesUseCase = mockk()
    private val configureNotificationsUseCase: ConfigureNotificationsUseCase = mockk()
    private lateinit var viewModel: CryptoAssetPanelViewModel

    private val testDispatcher = UnconfinedTestDispatcher()
    private val asset = CryptoAsset(symbol = "DOT")
    private val asset2 = CryptoAsset(symbol = "ADA")
    private val configuration = setOf(NotificationConfiguration(asset.symbol))

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
        every { getCryptoAssetsMarketInfoUseCase(any()) } returns flowOf(
            Result.Success(
                listOf(
                    marketAsset
                )
            )
        )
        coEvery { addFavouriteUseCase(any()) } returns Result.Success(Unit)
        coEvery { removeFavouriteUseCase(any()) } returns Result.Success(Unit)
        every { getLocalPreferencesUseCase() } returns flowOf(
            Result.Success(
                LocalPreferences(
                    notificationsConfiguration = configuration
                )
            )
        )
        coEvery { updateLocalPreferencesUseCase(any()) } returns Result.Success(Unit)
        coEvery { configureNotificationsUseCase(any()) } returns Result.Success(Unit)

        viewModel = CryptoAssetPanelViewModel(
            getCryptoAssetsMarketInfoUseCase,
            getFavouritesUseCase,
            addFavouriteUseCase,
            removeFavouriteUseCase,
            getLocalPreferencesUseCase,
            updateLocalPreferencesUseCase,
            configureNotificationsUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should be in favourites`() {
        // GIVEN
        coEvery { getCryptoAssetsMarketInfoUseCase(any()) } returns flowOf(
            Result.Success(
                listOf(
                    marketAsset2
                )
            )
        )
        val assetLiveData2 = viewModel.getCryptoAssetMarketInfo(asset2.symbol)
        assetLiveData2.observeForTesting {
            runTest {
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
        coEvery { getCryptoAssetsMarketInfoUseCase(any()) } returns flowOf(
            Result.Success(
                listOf(
                    marketAsset
                )
            )
        )
        val assetLiveData = viewModel.getCryptoAssetMarketInfo(asset.symbol)
        assetLiveData.observeForTesting {
            runTest {
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

    @Test
    fun `should notifications be enabled`() {
        // GIVEN
        val assetLiveData = viewModel.getCryptoAssetMarketInfo(asset.symbol)
        assetLiveData.observeForTesting {
            runTest {
                // WHEN
                val enabled = viewModel.areNotificationsEnabled().first()
                // THEN
                assertTrue(enabled)
            }
        }
    }

    @Test
    fun `should get configuration for crypto`() {
        // GIVEN
        val assetLiveData = viewModel.getCryptoAssetMarketInfo(asset.symbol)
        assetLiveData.observeForTesting {
            runTest {
                // WHEN
                val conf = viewModel.getConfigurationForCrypto().first()
                // THEN
                assertEquals(configuration.first(), conf)
            }
        }
    }

    @Test
    fun `should add crypto to notifications`() {
        // GIVEN
        val assetLiveData = viewModel.getCryptoAssetMarketInfo(asset.symbol)
        assetLiveData.observeForTesting {
            // WHEN
            viewModel.addCryptoToNotifications(null, null)
            // THEN
            coVerify {
                getLocalPreferencesUseCase()
                updateLocalPreferencesUseCase(any())
                configureNotificationsUseCase(setOf(NotificationConfiguration(asset.symbol, null, null)))
            }
        }
    }

    @Test
    fun `should remove crypto from notifications`() {
        // GIVEN
        val assetLiveData = viewModel.getCryptoAssetMarketInfo(asset.symbol)
        assetLiveData.observeForTesting {
            // WHEN
            viewModel.removeCryptoFromNotifications()
            // THEN
            coVerify {
                getLocalPreferencesUseCase()
                updateLocalPreferencesUseCase(any())
                configureNotificationsUseCase(emptySet())
            }
        }
    }
}
