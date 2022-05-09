package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserDataCache
import com.moonlightbutterfly.cryptohub.domain.models.UserData
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Test

class GetSignedInUserUseCaseTest {

    private val userDataCache: UserDataCache = mockk {
        every { getUserData() } returns UserData("test")
    }

    private val useCase = GetSignedInUserUseCase(userDataCache)

    @Test
    fun `should return user data`() {
        // WHEN
        val result = useCase()

        // THEN
        assertEquals("test", result?.userId)
    }
}
