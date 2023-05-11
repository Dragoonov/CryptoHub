package com.moonlightbutterfly.cryptohub.search

import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import com.moonlightbutterfly.cryptohub.usecases.AddRecentUseCase
import com.moonlightbutterfly.cryptohub.usecases.ClearRecentsUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetAllCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetRecentsUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveRecentUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    private val recentsFlow = MutableStateFlow(Answer.Success(CryptoCollection.EMPTY))
    private val getRecentsUseCase: GetRecentsUseCase = mockk()
    private val addRecentUseCase: AddRecentUseCase = mockk()
    private val removeRecentUseCase: RemoveRecentUseCase = mockk()
    private val clearRecentsUseCase: ClearRecentsUseCase = mockk()
    private val getAllCryptoAssetsMarketInfoUseCase: GetAllCryptoAssetsMarketInfoUseCase = mockk()

    private lateinit var viewModel: SearchViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        every { getRecentsUseCase() } returns recentsFlow
        coEvery { getAllCryptoAssetsMarketInfoUseCase(1) } returns flowOf(
            Answer.Success(
                listOf(
                    CryptoAssetMarketInfo(asset = CryptoAsset(symbol = "ada")),
                    CryptoAssetMarketInfo(asset = CryptoAsset(name = "cadard")),
                    CryptoAssetMarketInfo(asset = CryptoAsset(name = "polkadot")),
                )
            )
        )
        coEvery { getAllCryptoAssetsMarketInfoUseCase(not(1)) } returns flowOf(Answer.Success(listOf()))
        coEvery { addRecentUseCase(any()) } returns Answer.Success(Unit)
        coEvery { removeRecentUseCase(any()) } returns Answer.Success(Unit)
        coEvery { clearRecentsUseCase() } returns Answer.Success(Unit)
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchViewModel(
            getRecentsUseCase,
            addRecentUseCase,
            clearRecentsUseCase,
            getAllCryptoAssetsMarketInfoUseCase,
            removeRecentUseCase,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should not search by query less than 3 characters`() {
        // GIVEN WHEN
        viewModel.onQueryChange("as")

        // THEN
        assertEquals(0, viewModel.cryptoAssetsResults.value.size)
        coVerify(exactly = 0) {
            getAllCryptoAssetsMarketInfoUseCase(any())
        }
    }

    @Test
    fun `should search and filter by query of 3 characters`() = runTest(testDispatcher) {
        // WHEN
        viewModel.onQueryChange("ada")
        advanceTimeBy(1500)

        // THEN
        assertEquals(2, viewModel.cryptoAssetsResults.value.size)
        coVerify(exactly = 10) {
            getAllCryptoAssetsMarketInfoUseCase(any())
        }
    }

    @Test
    fun `should just add crypto asset to recents`() = runTest {
        // GIVEN
        recentsFlow.emit(Answer.Success(CryptoCollection.EMPTY))

        // WHEN
        viewModel.onResultClicked(CryptoAsset.EMPTY)

        // THEN
        coVerify {
            addRecentUseCase(CryptoAsset.EMPTY)
        }
        coVerify(exactly = 0) {
            removeRecentUseCase(any())
        }
    }

    @Test
    fun `should add crypto asset to recents and remove the same asset`() = runTest {
        // GIVEN
        val asset = CryptoAsset(name = "test")
        recentsFlow.emit(Answer.Success(CryptoCollection(cryptoAssets = listOf(asset))))
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.recents.collect {}
        }

        // WHEN
        viewModel.onResultClicked(asset)

        // THEN
        coVerify {
            removeRecentUseCase(asset)
            addRecentUseCase(asset)
        }
    }

    @Test
    fun `should add crypto asset to recents and remove last asset`() = runTest {
        // GIVEN
        val asset = CryptoAsset(name = "test")
        val asset2 = CryptoAsset(name = "test2")
        recentsFlow.emit(
            Answer.Success(
                CryptoCollection(
                    cryptoAssets =
                    listOf(
                        asset,
                        CryptoAsset.EMPTY,
                        CryptoAsset.EMPTY,
                        CryptoAsset.EMPTY,
                        CryptoAsset.EMPTY,
                        CryptoAsset.EMPTY,
                        CryptoAsset.EMPTY,
                        CryptoAsset.EMPTY,
                        CryptoAsset.EMPTY,
                        CryptoAsset.EMPTY,
                    )
                )
            )
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.recents.collect {}
        }

        // WHEN
        viewModel.onResultClicked(asset2)

        // THEN
        coVerify {
            removeRecentUseCase(asset)
            addRecentUseCase(asset2)
        }
    }

    @Test
    fun `should remove recents`() {
        // GIVEN WHEN
        viewModel.onDeleteRecentsClicked()

        // THEN
        coVerify {
            clearRecentsUseCase()
        }
    }
}
