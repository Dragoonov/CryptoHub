package com.moonlightbutterfly.cryptohub.framework.data.signin

import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.data.signin.TwitterSignInHandler
import com.moonlightbutterfly.cryptohub.models.User
import com.moonlightbutterfly.cryptohub.utils.toUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class TwitterSignInHandlerImpl(
    private val hostActivity: ComponentActivity,
    private val firebaseAuth: FirebaseAuth,
): SignInBaseHandler(), TwitterSignInHandler {

    override fun signIn(): Flow<Result<User>> {
        val pendingResultTask = firebaseAuth.pendingAuthResult
        if (pendingResultTask != null) {
            pendingResultTask
                .addOnSuccessListener { authResult ->
                    signInChannel.trySend(
                        authResult.user?.toUser()?.let {
                            Result.Success(it)
                        } ?: Result.Failure(error("Received null user"))
                    )
                }
                .addOnFailureListener {
                    signInChannel.trySend(Result.Failure(error(it.localizedMessage ?: "Something went wrong")))
                }
        } else {
            startSignInFlow()
        }
        return signInChannel.receiveAsFlow()
    }

    private fun startSignInFlow() {
        val provider = OAuthProvider.newBuilder("twitter.com")
        firebaseAuth
            .startActivityForSignInWithProvider(hostActivity, provider.build())
            .addOnSuccessListener { authResult ->
                signInChannel.trySend(
                    authResult.user?.toUser()?.let {
                        Result.Success(it)
                    } ?: Result.Failure(error("Received null user"))
                )
            }
            .addOnFailureListener {
                signInChannel.trySend(Result.Failure(error(it.localizedMessage ?: "Something went wrong")))
            }
    }
}