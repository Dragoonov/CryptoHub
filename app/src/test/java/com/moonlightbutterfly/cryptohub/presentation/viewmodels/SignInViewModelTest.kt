package com.moonlightbutterfly.cryptohub.presentation.viewmodels

import androidx.activity.ComponentActivity
import com.moonlightbutterfly.cryptohub.domain.models.LocalPreferences
import com.moonlightbutterfly.cryptohub.signincontrollers.GoogleSignInIntentController
import com.moonlightbutterfly.cryptohub.signincontrollers.SignInManager
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import com.moonlightbutterfly.cryptohub.usecases.SignInUserUseCase
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test

class SignInViewModelTest {

    private val signInUserUseCase: SignInUserUseCase = mockk()

    private val signInManager: SignInManager = mockk {
        every { isPhoneRequestInProcess } returns MutableStateFlow(false)
        every { signInThroughFacebook(any(), any(), any()) } just Runs
        every { signInThroughPhoneWithCode(any(), any()) } just Runs
        every { signInThroughPhone(any(), any(), any(), any()) } just Runs
        every { signInThroughEmail(any(), any(), any(), any(), any()) } just Runs
        every { signInThroughGoogle(any(), any(), any()) } just Runs
        every { signInThroughTwitter(any(), any(), any()) } just Runs
    }
    private val getLocalPreferencesUseCase: GetLocalPreferencesUseCase = mockk()
    private lateinit var viewModel: SignInViewModel

    private val onSignedIn: () -> Unit = {}
    private val onSignInFailed: (String) -> Unit = {}
    private val googleSignInIntentController: GoogleSignInIntentController = mockk()
    private val componentActivity: ComponentActivity = mockk()

    @Before
    fun setup() {
        every { getLocalPreferencesUseCase() } returns flowOf(LocalPreferences())
        viewModel = SignInViewModel(signInUserUseCase, signInManager, getLocalPreferencesUseCase)
    }

    @Test
    fun `should sign in through Google`() {
        // WHEN
        viewModel.signInThroughGoogle(onSignedIn, onSignInFailed, googleSignInIntentController)
        // THEN
        verify {
            signInManager.signInThroughGoogle(any(), onSignInFailed, googleSignInIntentController)
        }
    }

    @Test
    fun `should sign in through Facebook`() {
        // WHEN
        viewModel.signInThroughFacebook(onSignedIn, onSignInFailed, componentActivity)
        // THEN
        verify {
            signInManager.signInThroughFacebook(any(), onSignInFailed, componentActivity)
        }
    }

    @Test
    fun `should sign in through Twitter`() {
        // WHEN
        viewModel.signInThroughTwitter(onSignedIn, onSignInFailed, componentActivity)
        // THEN
        verify {
            signInManager.signInThroughTwitter(any(), onSignInFailed, componentActivity)
        }
    }

    @Test
    fun `should sign in through Email`() {
        // GIVEN
        val email = "test"
        val password = "pass_test"

        // WHEN
        viewModel.signInThroughEmail(email, password, onSignedIn, onSignInFailed, componentActivity)

        // THEN
        verify {
            signInManager.signInThroughEmail(email, password, any(), onSignInFailed, componentActivity)
        }
    }

    @Test
    fun `should sign in through Phone`() {
        // GIVEN
        val phone = "09"

        // WHEN
        viewModel.signInThroughPhone(phone, onSignedIn, onSignInFailed, componentActivity)

        // THEN
        verify {
            signInManager.signInThroughPhone(phone, any(), onSignInFailed, componentActivity)
        }
    }

    @Test
    fun `should sign in through Phone with code`() {
        // GIVEN
        val code = "09"

        // WHEN
        viewModel.signInThroughPhoneWithCode(code, componentActivity)

        // THEN
        verify {
            signInManager.signInThroughPhoneWithCode(code, componentActivity)
        }
    }
}
