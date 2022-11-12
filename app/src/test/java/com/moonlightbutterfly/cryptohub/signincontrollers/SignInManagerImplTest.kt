package com.moonlightbutterfly.cryptohub.signincontrollers

import androidx.activity.ComponentActivity
import com.moonlightbutterfly.cryptohub.models.User
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test

class SignInManagerImplTest {

    private val googleSignInIntentController: GoogleSignInIntentController = mockk()
    private val componentActivity: ComponentActivity = mockk()

    private val googleSignInController: GoogleSignInController = mockk {
        every { signIn(any(), any(), any()) } just Runs
    }
    private val emailSignInController: EmailSignInController = mockk {
        every { signIn(any(), any(), any(), any(), any()) } just Runs
    }
    private val phoneSignInController: PhoneSignInController = mockk {
        every { isPhoneRequestInProcess } returns MutableStateFlow(false)
        every { signIn(any(), any(), any(), any()) } just Runs
        every { signInWithCode(any(), any()) } just Runs
    }
    private val facebookSignInController: FacebookSignInController = mockk {
        every { signIn(any(), any(), any()) } just Runs
    }
    private val twitterSignInController: TwitterSignInController = mockk {
        every { signIn(any(), any(), any()) } just Runs
    }

    private val onSignedIn: (User) -> Unit = {}
    private val onSignInFailed: (String) -> Unit = {}

    private val signInManager = SignInManagerImpl(
        googleSignInController,
        emailSignInController,
        phoneSignInController,
        facebookSignInController,
        twitterSignInController
    )

    @Test
    fun `should sign in through Google`() {
        // WHEN
        signInManager.signInThroughGoogle(onSignedIn, onSignInFailed, googleSignInIntentController)
        // THEN
        verify {
            googleSignInController.signIn(onSignedIn, onSignInFailed, googleSignInIntentController)
        }
    }

    @Test
    fun `should sign in through Facebook`() {
        // WHEN
        signInManager.signInThroughFacebook(onSignedIn, onSignInFailed, componentActivity)
        // THEN
        verify {
            facebookSignInController.signIn(onSignedIn, onSignInFailed, componentActivity)
        }
    }

    @Test
    fun `should sign in through Twitter`() {
        // WHEN
        signInManager.signInThroughTwitter(onSignedIn, onSignInFailed, componentActivity)
        // THEN
        verify {
            twitterSignInController.signIn(onSignedIn, onSignInFailed, componentActivity)
        }
    }

    @Test
    fun `should sign in through Email`() {
        // GIVEN
        val email = "test"
        val password = "pass_test"

        // WHEN
        signInManager.signInThroughEmail(email, password, onSignedIn, onSignInFailed, componentActivity)

        // THEN
        verify {
            emailSignInController.signIn(email, password, onSignedIn, onSignInFailed, componentActivity)
        }
    }

    @Test
    fun `should sign in through Phone`() {
        // GIVEN
        val phone = "09"

        // WHEN
        signInManager.signInThroughPhone(phone, onSignedIn, onSignInFailed, componentActivity)

        // THEN
        verify {
            phoneSignInController.signIn(phone, onSignedIn, onSignInFailed, componentActivity)
        }
    }

    @Test
    fun `should sign in through Phone with code`() {
        // GIVEN
        val code = "09"

        // WHEN
        signInManager.signInThroughPhoneWithCode(code, componentActivity)

        // THEN
        verify {
            phoneSignInController.signInWithCode(code, componentActivity)
        }
    }
}
