package com.moonlightbutterfly.cryptohub.signincontrollers

import android.content.Intent
import com.firebase.ui.auth.AuthUI
import com.moonlightbutterfly.cryptohub.models.UserData

/**
 * Controller that's responsible for the Google sign in flow.
 */
class GoogleSignInController {

    /**
     * Launches sign in flow.
     * @param onSignInSuccess Callback to invoke if sign in flow ends successfully
     * @param onSignInFailure Callback to invoke if sign in flow ends with failure
     * @param googleSignInIntentController Controller returning the launcher
     */
    fun signIn(
        onSignInSuccess: (user: UserData) -> Unit,
        onSignInFailure: (message: String) -> Unit,
        googleSignInIntentController: GoogleSignInIntentController
    ) {
        val signInLauncher = googleSignInIntentController.getLauncher(onSignInSuccess, onSignInFailure)
        val intent = createSignInIntent()
        signInLauncher.launch(intent)
    }

    private fun createSignInIntent(): Intent {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
    }
}
