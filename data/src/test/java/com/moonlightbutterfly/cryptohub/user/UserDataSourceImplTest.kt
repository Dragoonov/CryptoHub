package com.moonlightbutterfly.cryptohub.user

import com.google.firebase.auth.FirebaseAuth
import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.data.common.getOrThrow
import com.moonlightbutterfly.cryptohub.models.User
import com.moonlightbutterfly.cryptohub.signin.FirebaseSignInHandler
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Test

class UserDataSourceImplTest {

    private val flow = MutableSharedFlow<Answer<User>>()
    private val firebaseSignInHandler: FirebaseSignInHandler = mockk {
        every { signIn() } returns flow
    }
    private val firebaseAuth: FirebaseAuth = mockk {
        every { currentUser } returns
            mockk {
                every { uid } returns "test"
                every { displayName } returns "test"
                every { email } returns "test"
            } andThen null
        every { signOut() } just Runs
    }

    private val dataSource = UserDataSourceImpl(
        firebaseSignInHandler, firebaseAuth
    )

    @Test
    fun `should get user`() {
        // WHEN
        var user = dataSource.getUser()

        // THEN
        assertTrue(user is Answer.Success)
        assertEquals("test", user.getOrThrow().userId)

        // WHEN
        user = dataSource.getUser()

        // THEN
        assertTrue(user is Answer.Failure)
    }

    @Test
    fun `should get the same sign in flow with different methods`() {
        // WHEN
        val googleFlow = dataSource.googleSignIn()
        val faceBookFlow = dataSource.facebookSignIn()
        val twitterFlow = dataSource.twitterSignIn()
        val emailFlow = dataSource.emailSignIn()
        val phoneFlow = dataSource.phoneSignIn()

        // THEN
        assertEquals(flow, googleFlow)
        assertEquals(flow, faceBookFlow)
        assertEquals(flow, twitterFlow)
        assertEquals(flow, emailFlow)
        assertEquals(flow, phoneFlow)
    }

    @Test
    fun `should sign out`() {
        // WHEN
        dataSource.signOut()

        // THEN
        verify {
            firebaseAuth.signOut()
        }
    }

    @Test
    fun `should test if user is signed in`() {
        // WHEN
        val isSignedIn = dataSource.isUserSignedIn()

        // THEN
        assertTrue(isSignedIn.getOrThrow())
    }
}
