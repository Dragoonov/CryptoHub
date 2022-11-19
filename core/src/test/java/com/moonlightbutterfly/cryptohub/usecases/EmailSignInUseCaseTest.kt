package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.data.UserRepository
import com.moonlightbutterfly.cryptohub.models.User
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Test

class EmailSignInUseCaseTest {

    private val userFlow = MutableSharedFlow<Result<User>>()
    private val userRepository: UserRepository = mockk {
        every { emailSignIn() } returns userFlow
    }

    private val useCase = EmailSignInUseCase(userRepository)

    @Test
    fun `should sign in through email`() {
        // WHEN
        val result = useCase()

        // THEN
        TestCase.assertEquals(userFlow, result)
    }
}
