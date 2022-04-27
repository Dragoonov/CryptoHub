package com.moonlightbutterfly.cryptohub.presentation.ui

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract

class SignInFlowProviderImpl(hostActivity: ComponentActivity) : SignInFlowProvider {

    private var signInActionOnSuccess: () -> Unit = {}
    private var signInActionOnFailure: () -> Unit = {}

    private val signInLauncher = hostActivity.registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            signInActionOnSuccess()
        } else {
            signInActionOnFailure()
        }
    }

    override fun getSignInIntentLauncher(
        onSignInSuccess: () -> Unit,
        onSignInFailure: () -> Unit
    ): ActivityResultLauncher<Intent> {
        signInActionOnFailure = onSignInFailure
        signInActionOnSuccess = onSignInSuccess
        return signInLauncher
    }
}
