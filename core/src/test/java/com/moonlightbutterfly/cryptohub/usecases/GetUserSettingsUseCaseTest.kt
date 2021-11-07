package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserConfigurationRepository
import com.moonlightbutterfly.cryptohub.domain.models.UserSettings
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class GetUserSettingsUseCaseTest {

    private val userSettings = flowOf(UserSettings.EMPTY)

    private val repositoryMock: UserConfigurationRepository = mockk {
        every { getUserSettings() } returns userSettings
    }

    private val useCase = GetUserSettingsUseCase(repositoryMock)

    @ExperimentalCoroutinesApi
    @Test
    fun `should return userSettings`() = runBlockingTest {
        // WHEN
        val settings = useCase().first()
        // THEN
        verify {
            repositoryMock.getUserSettings()
        }
        assertEquals(UserSettings.EMPTY, settings)
    }
}
