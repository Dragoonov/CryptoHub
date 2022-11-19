package com.moonlightbutterfly.cryptohub.presentation.viewmodels

import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.models.User
import com.moonlightbutterfly.cryptohub.presentation.SignInFacade
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SignInViewModelTest {

    private val flow = MutableSharedFlow<Result<User>>()
    private val signInFacade: SignInFacade = mockk {
        every { signIn() } returns flow
    }
    private val viewModel = SignInViewModel(signInFacade)

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should sign in`() {
        // WHEN
        viewModel.signIn()

        // THEN
        verify {
            signInFacade.signIn()
        }
    }
}
