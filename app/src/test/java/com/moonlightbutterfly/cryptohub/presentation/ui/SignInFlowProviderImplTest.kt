package com.moonlightbutterfly.cryptohub.presentation.ui

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Test

class SignInFlowProviderImplTest {

    private val launcher = mockk<ActivityResultLauncher<Intent>>()
    private val hostActivity = mockk<ComponentActivity> {
        every {
            registerForActivityResult<Intent, FirebaseAuthUIAuthenticationResult>(
                any(),
                any()
            )
        } returns launcher
    }

    private val signInFlowProvider = SignInFlowProviderImpl(hostActivity)

    @Test
    fun `should get proper intent launcher`() {
        // WHEN
        val returnedLauncher = signInFlowProvider.getSignInIntentLauncher({}, {})
        // THEN
        assertEquals(launcher, returnedLauncher)
    }
}
