package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.user.UserRepository
import com.moonlightbutterfly.cryptohub.models.User
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Test
import com.moonlightbutterfly.cryptohub.data.common.Result

class TwitterSignInUseCaseTest {

    private val userFlow = MutableSharedFlow<Result<User>>()
    private val userRepository: UserRepository = mockk {
        every { twitterSignIn() } returns userFlow
    }

    private val useCase = TwitterSignInUseCase(userRepository)

    @Test
    fun `should sign in through twitter`() {
        // WHEN
        val result = useCase()

        // THEN
        TestCase.assertEquals(userFlow, result)
    }
}
