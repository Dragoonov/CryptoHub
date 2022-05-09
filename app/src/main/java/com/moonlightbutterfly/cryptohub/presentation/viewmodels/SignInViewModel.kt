package com.moonlightbutterfly.cryptohub.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.moonlightbutterfly.cryptohub.domain.models.UserData
import com.moonlightbutterfly.cryptohub.signincontrollers.SignInManager
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import com.moonlightbutterfly.cryptohub.usecases.SignInUserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    private val signInUserUseCase: SignInUserUseCase,
    private val signInManager: SignInManager,
    getLocalPreferencesUseCase: GetLocalPreferencesUseCase
) : ViewModel() {

    val isPhoneRequestInProcess: Flow<Boolean> = signInManager.isPhoneRequestInProcess
    val isNightModeEnabled = getLocalPreferencesUseCase().map { it.nightModeEnabled }.asLiveData()

    private val signInAction: (() -> Unit) -> (userData: UserData) -> Unit = { function ->
        { userData ->
            signInUserUseCase(userData)
            function()
        }
    }

    fun signInThroughGoogle(
        onSignInSuccess: () -> Unit,
        onSignInFailure: (message: String) -> Unit
    ) {
        signInManager.signInThroughGoogle(
            signInAction(onSignInSuccess),
            onSignInFailure
        )
    }

    fun signInThroughEmail(
        email: String,
        password: String,
        onSignInSuccess: () -> Unit,
        onSignInFailure: (message: String) -> Unit
    ) {
        signInManager.signInThroughEmail(
            email,
            password,
            signInAction(onSignInSuccess),
            onSignInFailure
        )
    }

    fun signInThroughPhone(
        phoneNumber: String,
        onSignInSuccess: () -> Unit,
        onSignInFailure: (message: String) -> Unit,
    ) {
        signInManager.signInThroughPhone(
            phoneNumber,
            signInAction(onSignInSuccess),
            onSignInFailure
        )
    }

    fun signInThroughFacebook(
        onSignInSuccess: () -> Unit,
        onSignInFailure: (message: String) -> Unit
    ) {
        signInManager.signInThroughFacebook(
            signInAction(onSignInSuccess),
            onSignInFailure
        )
    }

    fun signInThroughTwitter(
        onSignInSuccess: () -> Unit,
        onSignInFailure: (message: String) -> Unit
    ) {
        signInManager.signInThroughTwitter(
            signInAction(onSignInSuccess),
            onSignInFailure
        )
    }

    fun signInThroughPhoneWithCode(code: String) {
        signInManager.signInThroughPhoneWithCode(code)
    }
}
