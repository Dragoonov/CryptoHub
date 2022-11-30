package com.moonlightbutterfly.cryptohub.presentation.signin

import com.moonlightbutterfly.cryptohub.usecases.EmailSignInUseCase
import com.moonlightbutterfly.cryptohub.usecases.FacebookSignInUseCase
import com.moonlightbutterfly.cryptohub.usecases.GoogleSignInUseCase
import com.moonlightbutterfly.cryptohub.usecases.PhoneSignInUseCase
import com.moonlightbutterfly.cryptohub.usecases.TwitterSignInUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class SignInFacadeTest {

    private val googleSignInUseCase: GoogleSignInUseCase = mockk()
    private val facebookSignInUseCase: FacebookSignInUseCase = mockk()
    private val twitterSignInUseCase: TwitterSignInUseCase = mockk()
    private val emailSignInUseCase: EmailSignInUseCase = mockk()
    private val phoneSignInUseCase: PhoneSignInUseCase = mockk()

    private val signInFacade = SignInFacade(
        googleSignInUseCase,
        emailSignInUseCase,
        phoneSignInUseCase,
        facebookSignInUseCase,
        twitterSignInUseCase
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
