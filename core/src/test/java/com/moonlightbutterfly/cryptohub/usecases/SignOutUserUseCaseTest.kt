package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.user.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import com.moonlightbutterfly.cryptohub.data.common.Result

class SignOutUserUseCaseTest {

    private val userRepository: UserRepository = mockk {
        every { signOut() } returns Result.Success(Unit)
    }

    private val useCase = SignOutUseCase(userRepository)

    @Test
    fun `should sign out user`() {
        // WHEN
        useCase()

        // THEN
        verify {
            userRepository.signOut()
        }
    }
}
