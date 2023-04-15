package com.moonlightbutterfly.cryptohub.data.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.data.common.Error
import com.moonlightbutterfly.cryptohub.data.user.FirebaseAuthDataProvider
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
) : FirebaseSignInHandler {
    private val signInChannel = Channel<Answer<User>>()

    private fun createSignInIntent(): Intent {
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(authProviders)
            .setTheme(firebaseAuthDataProvider.configurationData!!.theme)
            .setLogo(firebaseAuthDataProvider.configurationData!!.logo)
            .build()
    }

    override fun signIn(): Flow<Answer<User>> {
        firebaseAuthDataProvider.configurationData?.let {
            setSignInResult()
            val intent = createSignInIntent()
            it.intentLauncher.launch(intent)
        }
        return signInChannel.receiveAsFlow()
    }

    private fun setSignInResult() {
        firebaseAuthDataProvider.actionOnResult = { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                signInChannel.trySend(
                    firebaseAuth.currentUser?.toUser()?.let {
                        Answer.Success(it)
                    } ?: Answer.Failure(Error.NotFound("Received null user"))
                )
            } else {
                signInChannel.trySend(
                    Answer.Failure(
                        Error.Unknown(
                            result.idpResponse?.error?.localizedMessage
                                ?: "Something went wrong"
                        )
                    )
                )
            }
        }
    }
}
