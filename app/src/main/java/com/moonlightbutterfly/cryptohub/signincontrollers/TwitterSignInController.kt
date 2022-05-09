package com.moonlightbutterfly.cryptohub.signincontrollers

import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.domain.models.UserData
import com.moonlightbutterfly.cryptohub.utils.toUserData

/**
 * Controller that's responsible for the twitter sign in flow.
 * @param hostActivity Activity that hosts the flow
 * @param firebaseAuth [FirebaseAuth] instance that manages the flow
 */
class TwitterSignInController(private val hostActivity: ComponentActivity, private val firebaseAuth: FirebaseAuth) {

    fun signIn(onSignInSuccess: (UserData) -> Unit, onSignInFailure: (String) -> Unit) {
        val pendingResultTask = firebaseAuth.pendingAuthResult
        if (pendingResultTask != null) {
            pendingResultTask
                .addOnSuccessListener {
                    onSignInSuccess(it.user?.toUserData() ?: UserData())
                }
                .addOnFailureListener {
                    onSignInFailure(it.localizedMessage ?: hostActivity.getString(R.string.sign_in_failed))
                }
        } else {
            startSignInFlow(onSignInSuccess, onSignInFailure)
        }
    }

    private fun startSignInFlow(onSignInSuccess: (UserData) -> Unit, onSignInFailure: (String) -> Unit) {
        val provider = OAuthProvider.newBuilder("twitter.com")
        firebaseAuth
            .startActivityForSignInWithProvider(hostActivity, provider.build())
            .addOnSuccessListener {
                onSignInSuccess(it.user?.toUserData() ?: UserData())
            }
            .addOnFailureListener {
                onSignInFailure(it.localizedMessage ?: hostActivity.getString(R.string.sign_in_failed))
            }
    }
}
