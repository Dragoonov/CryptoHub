package com.moonlightbutterfly.cryptohub.signincontrollers

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.models.UserData
import com.moonlightbutterfly.cryptohub.utils.toUserData
import javax.inject.Inject

/**
 * Class responsible for configuring and returning the Google sign in launcher.
 */
class GoogleSignInIntentController @Inject constructor(hostActivity: ComponentActivity) {

    private lateinit var signInLauncher: ActivityResultLauncher<Intent>

    init {
        hostActivity.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                if (owner.lifecycle.currentState < Lifecycle.State.STARTED) {
                    signInLauncher = hostActivity.registerForActivityResult(
                        FirebaseAuthUIActivityResultContract()
                    ) { result ->
                        if (result.resultCode == AppCompatActivity.RESULT_OK) {
                            signInActionOnSuccess(
                                Firebase.auth.currentUser?.toUserData() ?: UserData()
                            )
                        } else {
                            signInActionOnFailure(
                                result.idpResponse?.error?.localizedMessage
                                    ?: hostActivity.getString(
                                        R.string.sign_in_failed
                                    )
                            )
                        }
                    }
                }
            }
        })
    }

    private var signInActionOnSuccess: (userData: UserData) -> Unit = {}
    private var signInActionOnFailure: (message: String) -> Unit = {}

    fun getLauncher(
        actionOnSuccess: (userData: UserData) -> Unit,
        actionOnFailure: (message: String) -> Unit
    ): ActivityResultLauncher<Intent> {
        signInActionOnSuccess = actionOnSuccess
        signInActionOnFailure = actionOnFailure
        return signInLauncher
    }
}
