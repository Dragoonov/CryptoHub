package com.moonlightbutterfly.cryptohub.presentation.viewmodels

import com.moonlightbutterfly.cryptohub.domain.models.LocalPreferences
import com.moonlightbutterfly.cryptohub.domain.models.UserData
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import com.moonlightbutterfly.cryptohub.usecases.UpdateLocalPreferencesUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
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
import org.junit.Test

@ExperimentalCoroutinesApi
class SettingsViewModelTest {
    private val getLocalPreferencesUseCase: GetLocalPreferencesUseCase = mockk()
    private val updateLocalPreferencesUseCase: UpdateLocalPreferencesUseCase = mockk()
    private val userData = spyk<UserData>()

    private lateinit var viewModel: SettingsViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        every { getLocalPreferencesUseCase() } returns flowOf(LocalPreferences.DEFAULT)
        coEvery { updateLocalPreferencesUseCase(any()) } just Runs
        Dispatchers.setMain(testDispatcher)
        viewModel = SettingsViewModel(getLocalPreferencesUseCase, updateLocalPreferencesUseCase, userData)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should update local preferences`() {
        // GIVEN
        every { getLocalPreferencesUseCase() } answers { flowOf(LocalPreferences.DEFAULT) }
        // WHEN
        viewModel.onNightModeChanged(true)
        // THEN
        coVerify {
            updateLocalPreferencesUseCase(any())
        }
    }

    @Test
    fun `should clear user data`() {
        // GIVEN WHEN
        viewModel.onSignedOut()

        // THEN
        verify {
            userData.clear()
        }
    }

    @Test
    fun `should check if user is signed in`() {
        // GIVEN
        userData.set(UserData("", "", ""))

        // WHEN
        var signedIn = viewModel.isUserSignedIn()

        // THEN
        assertFalse(signedIn)

        // GIVEN
        userData.set(UserData("test", "test", "test"))

        // WHEN
        signedIn = viewModel.isUserSignedIn()

        // THEN
        assertTrue(signedIn)
    }
}
