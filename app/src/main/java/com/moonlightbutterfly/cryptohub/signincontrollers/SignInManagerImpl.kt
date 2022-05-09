package com.moonlightbutterfly.cryptohub.signincontrollers

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
        onSignInFailure: (message: String) -> Unit
    ) {
        googleSignInController.signIn(onSignInSuccess, onSignInFailure)
    }

    override fun signInThroughEmail(
        email: String,
        password: String,
        onSignInSuccess: (user: UserData) -> Unit,
        onSignInFailure: (message: String) -> Unit
    ) {
        emailSignInController.signIn(email, password, onSignInSuccess, onSignInFailure)
    }

    override fun signInThroughPhone(
        phoneNumber: String,
        onSignInSuccess: (user: UserData) -> Unit,
        onSignInFailure: (message: String) -> Unit,
    ) {
        phoneSignInController.signIn(phoneNumber, onSignInSuccess, onSignInFailure)
    }

    override fun signInThroughPhoneWithCode(
        code: String
    ) {
        phoneSignInController.signInWithCode(code)
    }

    override fun signInThroughFacebook(
        onSignInSuccess: (user: UserData) -> Unit,
        onSignInFailure: (message: String) -> Unit
    ) {
        facebookSignInController.signIn(onSignInSuccess, onSignInFailure)
    }

    override fun signInThroughTwitter(
        onSignInSuccess: (user: UserData) -> Unit,
        onSignInFailure: (message: String) -> Unit
    ) {
        twitterSignInController.signIn(onSignInSuccess, onSignInFailure)
    }
}
