package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.user.UserRepository
import com.moonlightbutterfly.cryptohub.models.User
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Test
import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.data.common.getOrThrow

class GetSignedInUserUseCaseTest {

    private val userRepository: UserRepository = mockk {
        every { getUser() } returns Result.Success(User("test"))
    }

    private val useCase = GetSignedInUserUseCase(userRepository)

    @Test
    fun `should return user data`() {
        // WHEN
        val result = useCase()

        // THEN
        assertEquals("test", result.getOrThrow().userId)
    }
}
