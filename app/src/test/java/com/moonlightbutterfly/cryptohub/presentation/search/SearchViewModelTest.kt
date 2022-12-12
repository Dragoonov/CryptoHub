package com.moonlightbutterfly.cryptohub.presentation.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo
import com.moonlightbutterfly.cryptohub.models.CryptoCollection
import com.moonlightbutterfly.cryptohub.usecases.AddRecentUseCase
import com.moonlightbutterfly.cryptohub.usecases.ClearRecentsUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetAllCryptoAssetsMarketInfoUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetRecentsUseCase
import com.moonlightbutterfly.cryptohub.usecases.RemoveRecentUseCase
import com.moonlightbutterfly.cryptohub.utils.observeForTesting
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
class SearchViewModelTest {

    private val recentsFlow = MutableStateFlow(Result.Success(CryptoCollection.EMPTY))
    private val getRecentsUseCase: GetRecentsUseCase = mockk()
    private val addRecentUseCase: AddRecentUseCase = mockk()
    private val removeRecentUseCase: RemoveRecentUseCase = mockk()
    private val clearRecentsUseCase: ClearRecentsUseCase = mockk()
    private val getAllCryptoAssetsMarketInfoUseCase: GetAllCryptoAssetsMarketInfoUseCase = mockk()

    private lateinit var viewModel: SearchViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        every { getRecentsUseCase() } returns recentsFlow
        coEvery { getAllCryptoAssetsMarketInfoUseCase(1) } returns flowOf(
            Result.Success(
                listOf(
                    CryptoAssetMarketInfo(asset = CryptoAsset(symbol = "ada")),
                    CryptoAssetMarketInfo(asset = CryptoAsset(name = "cadard")),
                    CryptoAssetMarketInfo(asset = CryptoAsset(name = "polkadot")),
                )
            )
        )
        coEvery { getAllCryptoAssetsMarketInfoUseCase(not(1)) } returns flowOf(Result.Success(listOf()))
        coEvery { addRecentUseCase(any()) } returns Result.Success(Unit)
        coEvery { removeRecentUseCase(any()) } returns Result.Success(Unit)
        coEvery { clearRecentsUseCase() } returns Result.Success(Unit)
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
        viewModel.cryptoAssetsResults.observeForTesting {
            // GIVEN WHEN
            viewModel.onQueryChange("as")

            // THEN
            assertEquals(0, viewModel.cryptoAssetsResults.value!!.size)
            coVerify(exactly = 0) {
                getAllCryptoAssetsMarketInfoUseCase(any())
            }
        }
    }

    @Test
    fun `should search and filter by query of 3 characters`() =
        viewModel.cryptoAssetsResults.observeForTesting {
            runBlockingTest(testDispatcher) {
                // WHEN
                viewModel.onQueryChange("ada")
                advanceTimeBy(1500)

                // THEN
                assertEquals(2, viewModel.cryptoAssetsResults.value!!.size)
                coVerify(exactly = 10) {
                    getAllCryptoAssetsMarketInfoUseCase(any())
                }
            }
        }

    @Test
    fun `should just add crypto asset to recents`() = viewModel.recents.observeForTesting {
        runBlockingTest {
            // GIVEN
            recentsFlow.emit(Result.Success(CryptoCollection.EMPTY))

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
    }

    @Test
    fun `should add crypto asset to recents and remove the same asset`() =
        viewModel.recents.observeForTesting {
            runBlockingTest {
                // GIVEN
                val asset = CryptoAsset(name = "test")
                recentsFlow.emit(Result.Success(CryptoCollection(cryptoAssets = listOf(asset))))

                // WHEN
                viewModel.onResultClicked(asset)

                // THEN
                coVerify {
                    removeRecentUseCase(asset)
                    addRecentUseCase(asset)
                }
            }
        }

    @Test
    fun `should add crypto asset to recents and remove last asset`() =
        viewModel.recents.observeForTesting {
            runBlockingTest {
                // GIVEN
                val asset = CryptoAsset(name = "test")
                val asset2 = CryptoAsset(name = "test2")
                recentsFlow.emit(
                    Result.Success(
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

                // WHEN
                viewModel.onResultClicked(asset2)

                // THEN
                coVerify {
                    removeRecentUseCase(asset)
                    addRecentUseCase(asset2)
                }
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
