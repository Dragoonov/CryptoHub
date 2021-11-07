package com.moonlightbutterfly.cryptohub.presentation.viewmodels

import com.moonlightbutterfly.cryptohub.domain.models.UserSettings
import com.moonlightbutterfly.cryptohub.usecases.GetUserSettingsUseCase
import com.moonlightbutterfly.cryptohub.usecases.UpdateUserSettingsUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SettingsViewModelTest {
    private val getUserSettingsUseCase: GetUserSettingsUseCase = mockk()
    private val updateUserSettingsUseCase: UpdateUserSettingsUseCase = mockk()

    private lateinit var viewModel: SettingsViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        every { getUserSettingsUseCase() } returns flowOf(UserSettings.EMPTY)
        coEvery { updateUserSettingsUseCase(any()) } just Runs
        Dispatchers.setMain(testDispatcher)
        viewModel = SettingsViewModel(getUserSettingsUseCase, updateUserSettingsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should update user settings`() {
        // GIVEN
        every { getUserSettingsUseCase() } answers { flowOf(UserSettings.EMPTY) }
        // WHEN
        viewModel.onNightModeChanged(true)
        // THEN
        coVerify {
            updateUserSettingsUseCase(any())
        }
    }
}
