package com.moonlightbutterfly.cryptohub.framework.data.signin

import androidx.activity.ComponentActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.data.signin.FacebookSignInHandler
import com.moonlightbutterfly.cryptohub.models.User
import com.moonlightbutterfly.cryptohub.utils.toUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class FacebookSignInHandlerImpl(
    private val hostActivity: ComponentActivity,
    private val loginManager: LoginManager,
    private val firebaseAuth: FirebaseAuth
): SignInBaseHandler(), FacebookSignInHandler {
    override fun signIn(): Flow<Result<User>> {
        val callbackManager = CallbackManager.Factory.create()
        loginManager.apply {
            registerCallback(
                callbackManager,
                getFacebookCallback(hostActivity)
            )
            logInWithReadPermissions(
                hostActivity,
                callbackManager,
                listOf("public_profile", "email")
            )
        }
        return signInChannel.receiveAsFlow()
    }

    private fun getFacebookCallback(
        componentActivity: ComponentActivity
    ): FacebookCallback<LoginResult> {
        return object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                signInChannel.trySend(Result.Failure(error("Sign in cancelled")))
            }

            override fun onError(error: FacebookException) {
                signInChannel.trySend(Result.Failure(error( error.localizedMessage ?: "Sign in cancelled")))
            }

            override fun onSuccess(result: LoginResult) {
                handleFacebookAccessToken(
                    result.accessToken,
                    componentActivity
                )
            }
        }
    }

    private fun handleFacebookAccessToken(
        token: AccessToken,
        componentActivity: ComponentActivity
    ) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(componentActivity) { task ->
                if (task.isSuccessful) {
                    signInChannel.trySend(
                        firebaseAuth.currentUser?.toUser()?.let {
                            Result.Success(it)
                        } ?: Result.Failure(error("Received null user"))
                    )
                } else {
                    signInChannel.trySend(Result.Failure(error(task.exception?.message ?: "Something went wrong")))
                }
            }
    }
}