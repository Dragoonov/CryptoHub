package com.moonlightbutterfly.cryptohub.data.user

import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.data.common.getOrThrow
import com.moonlightbutterfly.cryptohub.models.User
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class UserRepositoryTest {

    private val userFlow = flowOf(Answer.Success(User("test")))
    private val userDataSource = mockk<UserDataSource> {
        every { googleSignIn() } returns userFlow
        every { facebookSignIn() } returns userFlow
        every { twitterSignIn() } returns userFlow
        every { emailSignIn() } returns userFlow
        every { phoneSignIn() } returns userFlow
        every { signOut() } returns Answer.Success(Unit)
        every { isUserSignedIn() } returns Answer.Success(true)
        every { getUser() } returns Answer.Success(User("test"))
    }

    private val userRepository = UserRepository(userDataSource)

    @ExperimentalCoroutinesApi
    @Test
    fun `should get userFlow after google sign in`() = runBlockingTest {
        // WHEN
        val result = userRepository.googleSignIn().first()

        // THEN
        verify {
            userDataSource.googleSignIn()
        }
        assertEquals("test", result.getOrThrow().userId)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should get userFlow after facebook sign in`() = runBlockingTest {
        // WHEN
        val result = userRepository.facebookSignIn().first()

        // THEN
        verify {
            userDataSource.facebookSignIn()
        }
        assertEquals("test", result.getOrThrow().userId)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should get userFlow after twitter sign in`() = runBlockingTest {
        // WHEN
        val result = userRepository.twitterSignIn().first()

        // THEN
        verify {
            userDataSource.twitterSignIn()
        }
        assertEquals("test", result.getOrThrow().userId)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should get userFlow after email sign in`() = runBlockingTest {
        // WHEN
        val result = userRepository.emailSignIn().first()

        // THEN
        verify {
            userDataSource.emailSignIn()
        }
        assertEquals("test", result.getOrThrow().userId)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should get userFlow after phone sign in`() = runBlockingTest {
        // WHEN
        val result = userRepository.phoneSignIn().first()

        // THEN
        verify {
            userDataSource.phoneSignIn()
        }
        assertEquals("test", result.getOrThrow().userId)
    }

    @Test
    fun `should sign out`() {
        // WHEN
        val result = userRepository.signOut()

        verify {
            userDataSource.signOut()
        }
        assertTrue(result is Answer.Success)
    }

    @Test
    fun `should get if user is signed in`() {
        val result = userRepository.isUserSignedIn()

        verify {
            userDataSource.isUserSignedIn()
        }
        assertTrue(result.getOrThrow())
    }

    @Test
    fun `should get user`() {
        val result = userRepository.getUser()

        verify {
            userDataSource.getUser()
        }
        assertEquals("test", result.getOrThrow().userId)
    }
}
