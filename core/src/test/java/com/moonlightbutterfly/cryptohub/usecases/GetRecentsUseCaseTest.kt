package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserConfigurationRepository
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class GetRecentsUseCaseTest {

    private val item1 = CryptoAsset("test1", "ts1", "test1")
    private val item2 = CryptoAsset("test2", "ts2", "test2")

    private val recents = flowOf(listOf(item1, item2))

    private val repositoryMock: UserConfigurationRepository = mockk {
        every { getRecents() } returns recents
    }
    private val useCase = GetRecentsUseCase(repositoryMock)

    @ExperimentalCoroutinesApi
    @Test
    fun `should get recents`() = runBlockingTest {
        // GIVEN // WHEN
        val recentList = useCase().first()
        // THEN
        verify {
            repositoryMock.getRecents()
        }
        assertEquals(listOf(item1, item2), recentList)
    }
}
