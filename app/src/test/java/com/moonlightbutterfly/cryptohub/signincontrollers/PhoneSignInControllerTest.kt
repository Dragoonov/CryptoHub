package com.moonlightbutterfly.cryptohub.signincontrollers

import android.util.Log
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class PhoneSignInControllerTest {

    private val hostActivity: ComponentActivity = mockk {
        every { getString(any()) } returns "Field cannot be empty"
    }
    private val firebaseAuth: FirebaseAuth = mockk {
        every { signInWithCredential(any()) } returns mockk(relaxed = true)
    }

    private val phoneAuthCredential: PhoneAuthCredential = mockk()

    private val phoneSignInController = PhoneSignInController(
        hostActivity,
        firebaseAuth,
    )

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.isLoggable(any(), any()) } returns false
        mockkStatic(PhoneAuthOptions::class)
        mockkStatic(PhoneAuthProvider::class)
        every {
            PhoneAuthProvider.verifyPhoneNumber(any())
        } just Runs
        every { PhoneAuthProvider.getCredential(any(), any()) } returns phoneAuthCredential
        every {
            PhoneAuthOptions.newBuilder(any())
        } returns mockk(relaxed = true)
    }

    @Test
    fun `should not sign in with empty phone number`() {
        // GIVEN
        val onSignInFailure: (String) -> Unit = mockk(relaxed = true)

        // WHEN
        phoneSignInController.signIn("", {}, onSignInFailure)

        // THEN
        verify {
            onSignInFailure("Field cannot be empty")
        }
    }

    @Test
    fun `should sign in with phone number`() {
        // WHEN
        phoneSignInController.signIn("89", {}, {})

        // THEN
        verify {
            PhoneAuthProvider.verifyPhoneNumber(any())
        }
    }

    @Test
    fun `should sign in with code`() {
        // WHEN
        phoneSignInController.signInWithCode("89")

        // THEN
        verify {
            firebaseAuth.signInWithCredential(phoneAuthCredential)
        }
    }
}
