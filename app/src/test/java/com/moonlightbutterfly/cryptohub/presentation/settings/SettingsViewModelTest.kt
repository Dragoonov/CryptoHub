package com.moonlightbutterfly.cryptohub.presentation.settings

import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import com.moonlightbutterfly.cryptohub.presentation.settings.SettingsViewModel
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import com.moonlightbutterfly.cryptohub.usecases.IsUserSignedInUseCase
import com.moonlightbutterfly.cryptohub.usecases.SignOutUseCase
import com.moonlightbutterfly.cryptohub.usecases.UpdateLocalPreferencesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SettingsViewModelTest {
    private val getLocalPreferencesUseCase: GetLocalPreferencesUseCase = mockk()
    private val updateLocalPreferencesUseCase: UpdateLocalPreferencesUseCase = mockk()
    private val signOutUserUseCase: SignOutUseCase = mockk()
    private val getSignedInUserUseCase: IsUserSignedInUseCase = mockk()

    private lateinit var viewModel: SettingsViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        every { getLocalPreferencesUseCase() } returns flowOf(Result.Success(LocalPreferences.DEFAULT))
        coEvery { updateLocalPreferencesUseCase(any()) } returns Result.Success(Unit)
        every { signOutUserUseCase() } returns Result.Success(Unit)
        every { getSignedInUserUseCase() } returns Result.Success(true) andThen Result.Success(false)
        Dispatchers.setMain(testDispatcher)
        viewModel = SettingsViewModel(
            getLocalPreferencesUseCase,
            updateLocalPreferencesUseCase,
            signOutUserUseCase,
            getSignedInUserUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should update local preferences`() {
        // GIVEN
        every { getLocalPreferencesUseCase() } answers { flowOf(Result.Success(LocalPreferences.DEFAULT)) }
        // WHEN
        viewModel.onNightModeChanged(true)
        // THEN
        coVerify {
            updateLocalPreferencesUseCase(any())
        }
    }

    @Test
    fun `should sign out user`() {
        // GIVEN WHEN
        viewModel.onSignedOut()

        // THEN
        verify {
            signOutUserUseCase()
        }
    }

    @Test
    fun `should check if user is signed in`() = runBlockingTest {
        // WHEN
        var signedIn = viewModel.isUserSignedIn.first()

        // THEN
        assertTrue(signedIn)

        // WHEN
        signedIn = viewModel.isUserSignedIn.last()

        // THEN
        assertFalse(signedIn)
    }
}
