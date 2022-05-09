package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserDataCache
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class SignOutUserUseCaseTest {

    private val userDataCache: UserDataCache = mockk {
        every { signOutUser() } just Runs
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
