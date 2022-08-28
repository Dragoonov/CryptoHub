package com.moonlightbutterfly.cryptohub.signincontrollers

import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.domain.models.UserData
import com.moonlightbutterfly.cryptohub.utils.toUserData

/**
 * Controller that's responsible for the twitter sign in flow.
 * @param firebaseAuth [FirebaseAuth] instance that manages the flow
 */
class TwitterSignInController(private val firebaseAuth: FirebaseAuth) {

    /**
     * Launches sign in flow.
     * @param onSignInSuccess Callback to invoke if sign in flow ends successfully
     * @param onSignInFailure Callback to invoke if sign in flow ends with failure
     * @param componentActivity Activity hosting the flow
     */
    fun signIn(
        onSignInSuccess: (UserData) -> Unit,
        onSignInFailure: (String) -> Unit,
        componentActivity: ComponentActivity
    ) {
        val pendingResultTask = firebaseAuth.pendingAuthResult
        if (pendingResultTask != null) {
            pendingResultTask
                .addOnSuccessListener {
                    onSignInSuccess(it.user?.toUserData() ?: UserData())
                }
                .addOnFailureListener {
                    onSignInFailure(it.localizedMessage ?: componentActivity.getString(R.string.sign_in_failed))
                }
        } else {
            startSignInFlow(onSignInSuccess, onSignInFailure, componentActivity)
        }
    }

    private fun startSignInFlow(
        onSignInSuccess: (UserData) -> Unit,
        onSignInFailure: (String) -> Unit,
        componentActivity: ComponentActivity
    ) {
        val provider = OAuthProvider.newBuilder("twitter.com")
        firebaseAuth
            .startActivityForSignInWithProvider(componentActivity, provider.build())
            .addOnSuccessListener {
                onSignInSuccess(it.user?.toUserData() ?: UserData())
            }
            .addOnFailureListener {
                onSignInFailure(it.localizedMessage ?: componentActivity.getString(R.string.sign_in_failed))
            }
    }
}
