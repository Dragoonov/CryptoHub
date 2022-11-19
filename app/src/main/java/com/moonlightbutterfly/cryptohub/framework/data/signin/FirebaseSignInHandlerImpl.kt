package com.moonlightbutterfly.cryptohub.framework.data.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.moonlightbutterfly.cryptohub.data.Error
import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.framework.FirebaseAuthDataProvider
import com.moonlightbutterfly.cryptohub.models.User
import com.moonlightbutterfly.cryptohub.utils.toUser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class FirebaseSignInHandlerImpl @Inject constructor(
    private val firebaseAuthDataProvider: FirebaseAuthDataProvider,
    private val firebaseAuth: FirebaseAuth,
    private val authProviders: List<AuthUI.IdpConfig>
): FirebaseSignInHandler {
    private val signInChannel = Channel<Result<User>>()

    private fun createSignInIntent(): Intent {
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(authProviders)
            .setTheme(firebaseAuthDataProvider.getConfigurationData().theme)
            .build()
    }

    override fun signIn(): Flow<Result<User>> {
        setSignInResult()
        val intent = createSignInIntent()
        firebaseAuthDataProvider.getConfigurationData().intentLauncher.launch(intent)
        return signInChannel.receiveAsFlow()
    }

    private fun setSignInResult() {
        firebaseAuthDataProvider.setActionOnResult { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                signInChannel.trySend(
                    firebaseAuth.currentUser?.toUser()?.let {
                        Result.Success(it)
                    } ?: Result.Failure(Error.NotFound("Received null user"))
                )
            } else {
                signInChannel.trySend(Result.Failure(Error.Unknown(result.idpResponse?.error?.localizedMessage
                    ?: "Something went wrong")))
            }
        }
    }
}