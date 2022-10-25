package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.data.UserDataCache
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class SignOutUserUseCaseTest {

    private val userDataCache: UserDataCache = mockk {
        every { signOutUser() } returns Result.Success(Unit)
    }

    private val useCase = SignOutUserUseCase(userDataCache)

    @Test
    fun `should sign out user`() {
        // WHEN
        useCase()

        // THEN
        verify {
            userDataCache.signOutUser()
        }
    }
}
