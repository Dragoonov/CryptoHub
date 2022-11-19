package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.data.UserRepository
import com.moonlightbutterfly.cryptohub.models.User
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Test

class GoogleSignInUseCaseTest {

    private val userFlow = MutableSharedFlow<Result<User>>()
    private val userRepository: UserRepository = mockk {
        every { googleSignIn() } returns userFlow
    }

    private val useCase = GoogleSignInUseCase(userRepository)

    @Test
    fun `should sign in through google`() {
        // WHEN
        val result = useCase()

        // THEN
        TestCase.assertEquals(userFlow, result)
    }
}
