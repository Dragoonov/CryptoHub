package com.moonlightbutterfly.cryptohub.signincontrollers

import androidx.activity.ComponentActivity
import com.moonlightbutterfly.cryptohub.domain.models.UserData

class SignInManagerImpl(
    private val googleSignInController: GoogleSignInController,
    private val emailSignInController: EmailSignInController,
    private val phoneSignInController: PhoneSignInController,
    private val facebookSignInController: FacebookSignInController,
    private val twitterSignInController: TwitterSignInController
) : SignInManager {

    override val isPhoneRequestInProcess = phoneSignInController.isPhoneRequestInProcess

    override fun signInThroughGoogle(
        onSignInSuccess: (user: UserData) -> Unit,
        onSignInFailure: (message: String) -> Unit,
        googleSignInIntentController: GoogleSignInIntentController
    ) {
        googleSignInController.signIn(onSignInSuccess, onSignInFailure, googleSignInIntentController)
    }

    override fun signInThroughEmail(
        email: String,
        password: String,
        onSignInSuccess: (user: UserData) -> Unit,
        onSignInFailure: (message: String) -> Unit,
        componentActivity: ComponentActivity
    ) {
        emailSignInController.signIn(email, password, onSignInSuccess, onSignInFailure, componentActivity)
    }

    override fun signInThroughPhone(
        phoneNumber: String,
        onSignInSuccess: (user: UserData) -> Unit,
        onSignInFailure: (message: String) -> Unit,
        componentActivity: ComponentActivity
    ) {
        phoneSignInController.signIn(phoneNumber, onSignInSuccess, onSignInFailure, componentActivity)
    }

    override fun signInThroughPhoneWithCode(
        code: String,
        componentActivity: ComponentActivity
    ) {
        phoneSignInController.signInWithCode(code, componentActivity)
    }

    override fun signInThroughFacebook(
        onSignInSuccess: (user: UserData) -> Unit,
        onSignInFailure: (message: String) -> Unit,
        componentActivity: ComponentActivity
    ) {
        facebookSignInController.signIn(onSignInSuccess, onSignInFailure, componentActivity)
    }

    override fun signInThroughTwitter(
        onSignInSuccess: (user: UserData) -> Unit,
        onSignInFailure: (message: String) -> Unit,
        componentActivity: ComponentActivity
    ) {
        twitterSignInController.signIn(onSignInSuccess, onSignInFailure, componentActivity)
    }
}
