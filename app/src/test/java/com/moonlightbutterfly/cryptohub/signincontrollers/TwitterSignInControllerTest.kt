package com.moonlightbutterfly.cryptohub.signincontrollers

import androidx.activity.ComponentActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class TwitterSignInControllerTest {
    private val firebaseAuth: FirebaseAuth = mockk {
        every { startActivityForSignInWithProvider(any(), any()) } returns mockk(relaxed = true)
        every { pendingAuthResult } returns null
    }
    private val oAuthProvider: OAuthProvider = mockk()

    private val twitterSignInController = TwitterSignInController(firebaseAuth)

    @Before
    fun setup() {
        mockkStatic(OAuthProvider::class)
        every { OAuthProvider.newBuilder(any()) } returns mockk {
            every { build() } returns oAuthProvider
        }
    }

    @Test
    fun `should sign in`() {
        // GIVEN
        val hostActivity: ComponentActivity = mockk()

        // WHEN
        twitterSignInController.signIn({}, {}, hostActivity)

        // THEN
        verify {
            firebaseAuth.startActivityForSignInWithProvider(hostActivity, oAuthProvider)
        }
    }

    @Test
    fun `should not start sign in flow`() {
        // GIVEN
        every { firebaseAuth.pendingAuthResult } returns mockk<Task<AuthResult>>(relaxed = true)
        val hostActivity: ComponentActivity = mockk()

        // WHEN
        twitterSignInController.signIn({}, {}, hostActivity)

        // THEN
        verify(exactly = 0) {
            firebaseAuth.startActivityForSignInWithProvider(any(), any())
        }
    }
}
