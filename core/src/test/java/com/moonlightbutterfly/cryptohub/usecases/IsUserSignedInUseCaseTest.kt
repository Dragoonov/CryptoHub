package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.data.UserRepository
import com.moonlightbutterfly.cryptohub.data.getOrThrow
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Test

class IsUserSignedInUseCaseTest {

    private val userRepository: UserRepository = mockk {
        every { isUserSignedIn() } returns Result.Success(true)
    }

    private val useCase = IsUserSignedInUseCase(userRepository)

    @Test
    fun `should check if user is logged in`() {
        // WHEN
        val result = useCase()

        // THEN
        TestCase.assertEquals(true, result.getOrThrow())
    }
}