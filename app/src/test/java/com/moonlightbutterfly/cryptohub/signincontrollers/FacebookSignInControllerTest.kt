package com.moonlightbutterfly.cryptohub.signincontrollers

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultRegistryOwner
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class FacebookSignInControllerTest {

    private val hostActivity: ComponentActivity = mockk()
    private val loginManager: LoginManager = mockk {
        every { registerCallback(any(), any()) } just Runs
        every { logInWithReadPermissions(any<ActivityResultRegistryOwner>(), any(), any()) } just Runs
    }
    private val firebaseAuth: FirebaseAuth = mockk()

    private val facebookSignInController = FacebookSignInController(
        hostActivity,
        loginManager,
        firebaseAuth
    )

    @Test
    fun `should sign in`() {
        // WHEN
        facebookSignInController.signIn({}, {})

        // THEN
        verify {
            loginManager.registerCallback(any(), any())
            loginManager.logInWithReadPermissions(hostActivity, any(), listOf("public_profile", "email"))
        }
    }
}
