package com.moonlightbutterfly.cryptohub.signincontrollers

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import junit.framework.TestCase.assertEquals
import org.junit.Test

class GoogleSignInIntentControllerTest {
    private val slot = slot<DefaultLifecycleObserver>()
    private val ownLifecycle: Lifecycle = mockk {
        every { addObserver(capture(slot)) } just Runs
        every { currentState } returns Lifecycle.State.CREATED
    }
    private val launcher: ActivityResultLauncher<Intent> = mockk()
    private val hostActivity: ComponentActivity = mockk(relaxed = true) {
        every {
            registerForActivityResult(
                any<ActivityResultContract<Intent, FirebaseAuthUIAuthenticationResult>>(),
                any()
            )
        } returns launcher
        every { lifecycle } returns ownLifecycle
    }

    private val googleSignInIntentController = GoogleSignInIntentController(hostActivity)

    @Test
    fun `should return launcher`() {
        // GIVEN
        slot.captured.onCreate(hostActivity)

        // WHEN
        val result = googleSignInIntentController.getLauncher({}, {})

        // THEN
        assertEquals(launcher, result)
    }
}
