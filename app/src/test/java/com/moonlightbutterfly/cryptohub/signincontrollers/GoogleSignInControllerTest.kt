package com.moonlightbutterfly.cryptohub.signincontrollers

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.firebase.ui.auth.AuthUI
import com.moonlightbutterfly.cryptohub.domain.models.UserData
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class GoogleSignInControllerTest {
    private val intent: Intent = mockk()
    private val launcher: ActivityResultLauncher<Intent> = mockk {
        every { launch(any()) } just Runs
    }
    private val googleSignInIntentController: GoogleSignInIntentController = mockk {
        every { getLauncher(any(), any()) } returns launcher
    }

    private val googleSignInController = GoogleSignInController(googleSignInIntentController)

    @Before
    fun setup() {
        mockkStatic(AuthUI::class)
        every {
            AuthUI
                .getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(any())
                .build()
        } returns intent
        mockkConstructor(AuthUI.IdpConfig.GoogleBuilder::class)
        every { anyConstructed<AuthUI.IdpConfig.GoogleBuilder>().build() } returns mockk()
    }

    @Test
    fun `should sign in`() {
        // GIVEN
        val onSignInSuccess: (UserData) -> Unit = {}
        val onSignInFailure: (String) -> Unit = {}

        // WHEN
        googleSignInController.signIn(onSignInSuccess, onSignInFailure)

        // THEN
        verify {
            googleSignInIntentController.getLauncher(onSignInSuccess, onSignInFailure)
            launcher.launch(intent)
        }
    }
}
