package com.moonlightbutterfly.cryptohub.framework.data.signin

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth
import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.data.signin.PhoneSignInHandler
import com.moonlightbutterfly.cryptohub.models.User
import com.moonlightbutterfly.cryptohub.utils.toUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class PhoneSignInHandlerImpl(
    hostActivity: ComponentActivity,
    firebaseAuth: FirebaseAuth
): SignInBaseHandler(), PhoneSignInHandler {

    private lateinit var signInLauncher: ActivityResultLauncher<Intent>

    init {
        hostActivity.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                if (owner.lifecycle.currentState < Lifecycle.State.STARTED) {
                    signInLauncher = hostActivity.registerForActivityResult(
                        FirebaseAuthUIActivityResultContract()
                    ) { result ->
                        if (result.resultCode == AppCompatActivity.RESULT_OK) {
                            signInChannel.trySend(
                                firebaseAuth.currentUser?.toUser()?.let {
                                    Result.Success(it)
                                } ?: Result.Failure(error("Received null user"))
                            )
                        } else {
                            signInChannel.trySend(Result.Failure(error(result.idpResponse?.error?.localizedMessage ?: "Something went wrong")))
                        }
                    }
                }
            }
        })
    }

    private fun createSignInIntent(): Intent {
        val providers = arrayListOf(
            AuthUI.IdpConfig.PhoneBuilder().build()
        )
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
    }

    override fun signIn(): Flow<Result<User>> {
        val intent = createSignInIntent()
        signInLauncher.launch(intent)
        return signInChannel.receiveAsFlow()
    }

}