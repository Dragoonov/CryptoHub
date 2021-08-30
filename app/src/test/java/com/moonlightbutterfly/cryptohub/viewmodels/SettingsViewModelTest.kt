package com.moonlightbutterfly.cryptohub.viewmodels

import androidx.lifecycle.MutableLiveData
import com.moonlightbutterfly.cryptohub.repository.dataobjects.AppConfig
import com.moonlightbutterfly.cryptohub.usecases.GetAppConfigSettingsUseCase
import com.moonlightbutterfly.cryptohub.usecases.SetAppConfigUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SettingsViewModelTest {
    private val getAppConfigSettingsUseCase: GetAppConfigSettingsUseCase = mockk {
        every { isNightModeEnabled } answers { MutableLiveData(false) }
        every { appConfig } answers { MutableLiveData(AppConfig(5, false)) }
    }
    private val setAppConfigUseCase: SetAppConfigUseCase = mockk {
        coEvery { updateAppConfig(any()) } just Runs
    }

    private val viewModel = SettingsViewModel(getAppConfigSettingsUseCase, setAppConfigUseCase)

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should update app config`() {
        // WHEN
        viewModel.onNightModeChanged(true)
        // THEN
        coVerify(exactly = 1) {
            setAppConfigUseCase.updateAppConfig(any())
        }
    }
}
