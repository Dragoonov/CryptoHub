package com.moonlightbutterfly.cryptohub.signin

import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.models.User
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import com.moonlightbutterfly.cryptohub.usecases.IsUserSignedInUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SignInViewModelTest {

    private val flow = MutableSharedFlow<Answer<User>>()
    private val signInFacade: SignInFacade = mockk {
        every { signIn() } returns flow
    }
    private val isSignedInUser: IsUserSignedInUseCase = mockk()
    private val localPreferencesUseCase: GetLocalPreferencesUseCase = mockk()

    private lateinit var viewModel: SignInViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        every { localPreferencesUseCase() } returns flowOf()
        every { isSignedInUser() } returns Answer.Success(true)
        Dispatchers.setMain(testDispatcher)
        viewModel = SignInViewModel(signInFacade, isSignedInUser, localPreferencesUseCase)
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

    @Test
    fun `should check if user signed in`() {
        // GIVEN WHEN
        val isSignedIn = viewModel.isUserSignedIn()
        // THEN
        assertTrue(isSignedIn)
    }
}
