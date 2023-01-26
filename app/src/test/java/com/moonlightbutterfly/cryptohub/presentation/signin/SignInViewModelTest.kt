package com.moonlightbutterfly.cryptohub.presentation.signin

import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.models.User
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import com.moonlightbutterfly.cryptohub.usecases.IsUserSignedInUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
    private val isSignedInUser: IsUserSignedInUseCase = mockk(relaxed = true)
    private val localPreferencesUseCase: GetLocalPreferencesUseCase = mockk(relaxed = true)

    private val viewModel = SignInViewModel(signInFacade, isSignedInUser, localPreferencesUseCase)

    private val testDispatcher = UnconfinedTestDispatcher()

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
