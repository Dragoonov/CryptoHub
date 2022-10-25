package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.data.UserDataCache
import com.moonlightbutterfly.cryptohub.models.UserData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class SignInUserUseCaseTest {

    private val userDataCache: UserDataCache = mockk {
        every { signInUser(any()) } returns Result.Success(Unit)
    }

    private val useCase = SignInUserUseCase(userDataCache)

    @Test
    fun `should sign in user`() {
        // GIVEN
        val data = UserData("test")
        // WHEN
        useCase(data)

        // THEN
        verify {
            userDataCache.signInUser(data)
        }
    }
}
