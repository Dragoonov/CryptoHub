package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.data.notifications.Notifier
import com.moonlightbutterfly.cryptohub.models.NotificationConfiguration
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class ConfigureNotificationsUseCaseTest {

    private val notifier: Notifier = mockk {
        every { configure(any()) } returns Result.Success(Unit)
    }

    private val useCase = ConfigureNotificationsUseCase(notifier)

    @ExperimentalCoroutinesApi
    @Test
    fun `should configure notifications`() = runBlockingTest {
        // GIVEN
        val set = setOf(NotificationConfiguration("test"))
        // WHEN
        useCase(set)

        // THEN
        coVerify {
            notifier.configure(set)
        }
    }
}
