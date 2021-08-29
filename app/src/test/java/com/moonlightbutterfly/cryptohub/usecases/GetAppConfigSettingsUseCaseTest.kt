package com.moonlightbutterfly.cryptohub.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.moonlightbutterfly.cryptohub.repository.CryptoHubInternalRepository
import database.AppConfig
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetAppConfigSettingsUseCaseTest {

    private val repositoryMock: CryptoHubInternalRepository = mockk {
        every { appConfig } answers { appConfigLiveData }
    }
    private val appConfigLiveData = MutableLiveData(AppConfig(5, true))

    private val useCase = GetAppConfigSettingsUseCase(repositoryMock)
    private val appConfigObserver: Observer<AppConfig> = Observer { }
    private val nightModeObserver: Observer<Boolean> = Observer { }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        useCase.appConfig.observeForever(appConfigObserver)
        useCase.isNightModeEnabled.observeForever(nightModeObserver)
    }

    @After
    fun tearDown() {
        useCase.appConfig.removeObserver(appConfigObserver)
        useCase.isNightModeEnabled.removeObserver(nightModeObserver)
    }

    @Test
    fun `should return appConfig`() {
        // WHEN
        val appConfig = useCase.appConfig
        // THEN
        assertEquals(appConfigLiveData, appConfig)
    }

    @Test
    fun `should return proper night mode`() {
        // WHEN
        val nightMode = useCase.isNightModeEnabled
        // THEN
        assertTrue(nightMode.value!!)
    }
}
