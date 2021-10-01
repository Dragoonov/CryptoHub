package com.moonlightbutterfly.cryptohub.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.moonlightbutterfly.cryptohub.dataobjects.AppConfig
import com.moonlightbutterfly.cryptohub.repository.CryptoHubInternalRepository
import com.moonlightbutterfly.cryptohub.utils.observeForTesting
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class GetAppConfigSettingsUseCaseTest {

    private val repositoryMock: CryptoHubInternalRepository = mockk {
        every { appConfig } answers { appConfigLiveData }
    }
    private val appConfigLiveData = MutableLiveData(AppConfig(5, true))

    private val useCase = GetAppConfigSettingsUseCase(repositoryMock)

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `should return appConfig`() {
        // WHEN
        val appConfig = useCase.appConfig
        // THEN
        assertEquals(appConfigLiveData, appConfig)
    }

    @Test
    fun `should return proper night mode`() = useCase.isNightModeEnabled.observeForTesting {
        // WHEN
        val nightMode = useCase.isNightModeEnabled
        // THEN
        assertTrue(nightMode.value!!)
    }
}
