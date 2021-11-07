package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserConfigurationRepository
import com.moonlightbutterfly.cryptohub.domain.models.UserSettings
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class UpdateUserSettingsUseCaseTest {

    private val repositoryMock: UserConfigurationRepository = mockk {
        coEvery { updateUserSettings(any()) } just Runs
    }
    private val useCase = UpdateUserSettingsUseCase(repositoryMock)

    @ExperimentalCoroutinesApi
    @Test
    fun `should update appConfig`() = runBlockingTest {
        // GIVEN
        val settings = UserSettings(true)
        // WHEN
        useCase(settings)
        // THEN
        coVerify {
            repositoryMock.updateUserSettings(settings)
        }
    }
}
