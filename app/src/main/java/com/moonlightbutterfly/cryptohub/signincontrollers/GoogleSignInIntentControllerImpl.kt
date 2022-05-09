package com.moonlightbutterfly.cryptohub.signincontrollers

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.domain.models.UserData
import com.moonlightbutterfly.cryptohub.utils.toUserData

class GoogleSignInIntentControllerImpl(hostActivity: ComponentActivity) : GoogleSignInIntentController {

    private var signInActionOnSuccess: (userData: UserData) -> Unit = {}
    private var signInActionOnFailure: (message: String) -> Unit = {}

    private val signInLauncher = hostActivity.registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            signInActionOnSuccess(Firebase.auth.currentUser?.toUserData() ?: UserData())
        } else {
            signInActionOnFailure(
                result.idpResponse?.error?.localizedMessage ?: hostActivity.getString(
                    R.string.sign_in_failed
                )
            )
        }
    }

    override fun getLauncher(
        actionOnSuccess: (userData: UserData) -> Unit,
        actionOnFailure: (message: String) -> Unit
    ): ActivityResultLauncher<Intent> {
        signInActionOnSuccess = actionOnSuccess
        signInActionOnFailure = actionOnFailure
        return signInLauncher
    }
}
