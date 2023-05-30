package com.moonlightbutterfly.cryptohub.panel

import androidx.lifecycle.SavedStateHandle
import com.moonlightbutterfly.cryptohub.data.common.Answer
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
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
    private val stateHandle = mockk<SavedStateHandle>()
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { getFavouritesUseCase() } returns flowOf(
            Answer.Success(
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
            Answer.Success(
                listOf(
                    marketAsset
                )
            )
        )
        coEvery { addFavouriteUseCase(any()) } returns Answer.Success(Unit)
        coEvery { removeFavouriteUseCase(any()) } returns Answer.Success(Unit)
        every { getLocalPreferencesUseCase() } returns flowOf(
            Answer.Success(
                LocalPreferences(
                    notificationsConfiguration = configuration
                )
            )
        )
        coEvery { updateLocalPreferencesUseCase(any()) } returns Answer.Success(Unit)
        coEvery { configureNotificationsUseCase(any()) } returns Answer.Success(Unit)
        every { stateHandle.get<String>("symbol") } returns asset.symbol
        viewModel = CryptoAssetPanelViewModel(
            getCryptoAssetsMarketInfoUseCase,
            getFavouritesUseCase,
            addFavouriteUseCase,
            removeFavouriteUseCase,
            getLocalPreferencesUseCase,
            updateLocalPreferencesUseCase,
            configureNotificationsUseCase,
            stateHandle,
            CryptoAssetPanelUIState()
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should be in favourites`() = runTest {
        // GIVEN
        coEvery { getCryptoAssetsMarketInfoUseCase(any()) } returns flowOf(
            Answer.Success(
                listOf(
                    marketAsset2
                )
            )
        )
        every { stateHandle.get<String>("symbol") } returns asset2.symbol
        viewModel = CryptoAssetPanelViewModel(
            getCryptoAssetsMarketInfoUseCase,
            getFavouritesUseCase,
            addFavouriteUseCase,
            removeFavouriteUseCase,
            getLocalPreferencesUseCase,
            updateLocalPreferencesUseCase,
            configureNotificationsUseCase,
            stateHandle,
            CryptoAssetPanelUIState()
        )
        // WHEN
        val check1 = viewModel.uiState.first().isInFavourites!!
        // THEN
        assertTrue(check1)
    }

    @Test
    fun `should not be in favourites`() = runTest {
        // GIVEN
        coEvery { getCryptoAssetsMarketInfoUseCase(any()) } returns flowOf(
            Answer.Success(
                listOf(
                    marketAsset
                )
            )
        )
        // WHEN
        val check1 = viewModel.uiState.first().isInFavourites!!
        // THEN
        assertFalse(check1)
    }

    @Test
    fun `should add to favourites`() = runTest {
        // GIVEN
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }
        // WHEN
        viewModel.acceptIntent(CryptoAssetPanelIntent.AddToFavourites)
        // THEN
        coVerify {
            addFavouriteUseCase(asset)
        }
    }

    @Test
    fun `should remove from favourites`() = runTest {
        // GIVEN
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }
        // WHEN
        viewModel.acceptIntent(CryptoAssetPanelIntent.RemoveFromFavourites)

        // THEN
        coVerify {
            removeFavouriteUseCase(asset)
        }
    }

    @Test
    fun `should notifications be enabled`() = runTest {
        // GIVEN WHEN
        val enabled = viewModel.uiState.first().notificationsEnabled!!
        // THEN
        assertTrue(enabled)
    }

    @Test
    fun `should get configuration for crypto`() = runTest {
        // GIVEN WHEN
        val conf = viewModel.uiState.first().notificationConfiguration
        // THEN
        assertEquals(configuration.first(), conf)
    }

    @Test
    fun `should add crypto to notifications`() = runTest {
        // GIVEN
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }
        // WHEN
        viewModel.acceptIntent(
            CryptoAssetPanelIntent.ScheduleNotifications(
                NotificationConfiguration("")
            )
        )
        // THEN
        coVerify {
            getLocalPreferencesUseCase()
            updateLocalPreferencesUseCase(any())
            configureNotificationsUseCase(
                setOf(
                    NotificationConfiguration(
                        asset.symbol,
                        null,
                        null
                    )
                )
            )
        }
    }

    @Test
    fun `should remove crypto from notifications`() = runTest {
        // GIVEN
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }
        // WHEN
        viewModel.acceptIntent(CryptoAssetPanelIntent.ClearNotifications)
        // THEN
        coVerify {
            getLocalPreferencesUseCase()
            updateLocalPreferencesUseCase(any())
            configureNotificationsUseCase(emptySet())
        }
    }
}
