package com.moonlightbutterfly.cryptohub.signincontrollers

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Test

class GoogleSignInIntentControllerImplTest {
    private val launcher: ActivityResultLauncher<Intent> = mockk()
    private val hostActivity: ComponentActivity = mockk {
        every {
            registerForActivityResult(
                any<ActivityResultContract<Intent, FirebaseAuthUIAuthenticationResult>>(),
                any()
            )
        } returns launcher
    }

    private val googleSignInIntentController = GoogleSignInIntentControllerImpl(hostActivity)

    @Test
    fun `should return launcher`() {
        // WHEN
        val result = googleSignInIntentController.getLauncher({}, {})

        // THEN
        assertEquals(launcher, result)
    }
}
