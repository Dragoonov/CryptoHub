package com.moonlightbutterfly.cryptohub.signincontrollers

import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.moonlightbutterfly.cryptohub.models.UserData
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class EmailSignInControllerTest {

    private val hostActivity: ComponentActivity = mockk {
        every { getString(any()) } returns "Fields cannot be empty"
    }

    private val firebaseAuth: FirebaseAuth = mockk {
        every { createUserWithEmailAndPassword(any(), any()) } returns mockk(relaxed = true)
    }

    private val onSignInSuccess: (UserData) -> Unit = mockk()
    private val onSignInFailure: (String) -> Unit = mockk()

    private val emailSignInController = EmailSignInController(
        firebaseAuth
    )

    @Before
    fun setup() {
        every { onSignInFailure(any()) } just Runs
    }

    @Test
    fun `should not sign in with empty email or password`() {
        // GIVEN
        val email = ""
        val password = ""

        // WHEN
        emailSignInController.signIn(email, "not_empty", onSignInSuccess, onSignInFailure, hostActivity)
        emailSignInController.signIn("not_empty", password, onSignInSuccess, onSignInFailure, hostActivity)

        // THEN
        verify(exactly = 2) {
            onSignInFailure("Fields cannot be empty")
        }
        verify(exactly = 0) {
            firebaseAuth.createUserWithEmailAndPassword(any(), any())
        }
    }

    @Test
    fun `should sign in`() {
        // GIVEN
        val email = "test"
        val password = "test"

        // WHEN
        emailSignInController.signIn(email, password, onSignInSuccess, onSignInFailure, hostActivity)

        // THEN
        verify {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
        }
    }
}
