package com.moonlightbutterfly.cryptohub.framework.data.signin

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.moonlightbutterfly.cryptohub.framework.FirebaseAuthDataProvider
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class FirebaseSignInHandlerImplTest {
    private val intent: Intent = mockk()
    private val launcher: ActivityResultLauncher<Intent> = mockk {
        every { launch(any()) } just Runs
    }
    private val firebaseAuthDataProvider: FirebaseAuthDataProvider = mockk {
        every { setActionOnResult(any()) } just Runs
        every { getConfigurationData() } returns FirebaseAuthDataProvider.ConfigurationData(launcher,
            0)
    }
    private val firebaseAuth: FirebaseAuth = mockk {
        every { currentUser } returns null
    }

    private val handler = FirebaseSignInHandlerImpl(firebaseAuthDataProvider, firebaseAuth, listOf())

    @Before
    fun setup() {
        mockkStatic(AuthUI::class)
        every {
            AuthUI
                .getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(any())
                .setTheme(any())
                .build()
        } returns intent
    }

    @Test
    fun `should sign in`() {
        // GIVEN WHEN
        handler.signIn()

        // THEN
        verify {
            firebaseAuthDataProvider.setActionOnResult(any())
            firebaseAuthDataProvider.getConfigurationData()
            launcher.launch(intent)
        }
    }
}
