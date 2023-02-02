package com.moonlightbutterfly.cryptohub.presentation.signin

import com.moonlightbutterfly.cryptohub.usecases.GoogleSignInUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class SignInFacadeTest {

    private val googleSignInUseCase: GoogleSignInUseCase = mockk()

    private val signInFacade = SignInFacade(
        googleSignInUseCase,
    )

    @Before
    fun setup() {
        every { googleSignInUseCase() } returns mockk()
    }

    @Test
    fun `should invoke google use case on sign in`() {
        // WHEN
        signInFacade.signIn()

        // THEN
        verify {
            googleSignInUseCase()
        }
    }
}
