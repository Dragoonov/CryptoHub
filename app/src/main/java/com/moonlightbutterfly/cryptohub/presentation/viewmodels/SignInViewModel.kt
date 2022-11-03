package com.moonlightbutterfly.cryptohub.presentation.viewmodels

import androidx.activity.ComponentActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.moonlightbutterfly.cryptohub.data.unpack
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import com.moonlightbutterfly.cryptohub.models.UserData
import com.moonlightbutterfly.cryptohub.signincontrollers.GoogleSignInIntentController
import com.moonlightbutterfly.cryptohub.signincontrollers.SignInManager
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import com.moonlightbutterfly.cryptohub.usecases.SignInUserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    private val signInUserUseCase: SignInUserUseCase,
    private val signInManager: SignInManager,
    getLocalPreferencesUseCase: GetLocalPreferencesUseCase
) : BaseViewModel() {

    val isPhoneRequestInProcess: Flow<Boolean> = signInManager.isPhoneRequestInProcess
    val isNightModeEnabled = getLocalPreferencesUseCase().propagateErrors()
        .map { it.unpack(LocalPreferences.DEFAULT).nightModeEnabled }.asLiveData()

    private val signInAction: (() -> Unit) -> (userData: UserData) -> Unit = { function ->
        { userData ->
            viewModelScope.launch {
                signInUserUseCase(userData).propagateErrors()
            }
            function()
        }
    }

    fun signInThroughGoogle(
        onSignInSuccess: () -> Unit,
        onSignInFailure: (message: String) -> Unit,
        googleSignInIntentController: GoogleSignInIntentController
    ) {
        signInManager.signInThroughGoogle(
            signInAction(onSignInSuccess),
            onSignInFailure,
            googleSignInIntentController
        )
    }

    fun signInThroughEmail(
        email: String,
        password: String,
        onSignInSuccess: () -> Unit,
        onSignInFailure: (message: String) -> Unit,
        componentActivity: ComponentActivity
    ) {
        signInManager.signInThroughEmail(
            email,
            password,
            signInAction(onSignInSuccess),
            onSignInFailure,
            componentActivity
        )
    }

    fun signInThroughPhone(
        phoneNumber: String,
        onSignInSuccess: () -> Unit,
        onSignInFailure: (message: String) -> Unit,
        componentActivity: ComponentActivity
    ) {
        signInManager.signInThroughPhone(
            phoneNumber,
            signInAction(onSignInSuccess),
            onSignInFailure,
            componentActivity
        )
    }

    fun signInThroughFacebook(
        onSignInSuccess: () -> Unit,
        onSignInFailure: (message: String) -> Unit,
        componentActivity: ComponentActivity
    ) {
        signInManager.signInThroughFacebook(
            signInAction(onSignInSuccess),
            onSignInFailure,
            componentActivity
        )
    }

    fun signInThroughTwitter(
        onSignInSuccess: () -> Unit,
        onSignInFailure: (message: String) -> Unit,
        componentActivity: ComponentActivity
    ) {
        signInManager.signInThroughTwitter(
            signInAction(onSignInSuccess),
            onSignInFailure,
            componentActivity
        )
    }

    fun signInThroughPhoneWithCode(code: String, componentActivity: ComponentActivity) {
        signInManager.signInThroughPhoneWithCode(code, componentActivity)
    }
}
