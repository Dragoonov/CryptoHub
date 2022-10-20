package com.moonlightbutterfly.cryptohub.signincontrollers

import androidx.activity.ComponentActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.models.UserData
import com.moonlightbutterfly.cryptohub.utils.toUserData

/**
 * Controller that's responsible for the facebook sign in flow.
 * @param loginManager [LoginManager] instance that manages the flow at Facebook side
 * @param firebaseAuth [FirebaseAuth] instance that manages the flow at Google side
 */
class FacebookSignInController(
    private val loginManager: LoginManager,
    private val firebaseAuth: FirebaseAuth
) {

    /**
     * Launches sign in flow.
     * @param onSignInSuccess Callback to invoke if sign in flow ends successfully
     * @param onSignInFailure Callback to invoke if sign in flow ends with failure
     * @param componentActivity Activity hosting the flow
     */
    fun signIn(
        onSignInSuccess: (user: UserData) -> Unit,
        onSignInFailure: (message: String) -> Unit,
        componentActivity: ComponentActivity
    ) {
        val callbackManager = CallbackManager.Factory.create()
        loginManager.apply {
            registerCallback(
                callbackManager,
                getFacebookCallback(onSignInSuccess, onSignInFailure, componentActivity)
            )
            logInWithReadPermissions(
                componentActivity,
                callbackManager,
                listOf("public_profile", "email")
            )
        }
    }

    private fun getFacebookCallback(
        onSignInSuccess: (user: UserData) -> Unit,
        onSignInFailure: (message: String) -> Unit,
        componentActivity: ComponentActivity
    ): FacebookCallback<LoginResult> {
        return object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                onSignInFailure(componentActivity.getString(R.string.sign_in_failed))
            }

            override fun onError(error: FacebookException) {
                onSignInFailure(
                    error.localizedMessage ?: componentActivity.getString(R.string.sign_in_failed)
                )
            }

            override fun onSuccess(result: LoginResult) {
                handleFacebookAccessToken(
                    result.accessToken,
                    onSignInSuccess,
                    onSignInFailure,
                    componentActivity
                )
            }
        }
    }

    private fun handleFacebookAccessToken(
        token: AccessToken,
        onSignInSuccess: (user: UserData) -> Unit,
        onSignInFailure: (message: String) -> Unit,
        componentActivity: ComponentActivity
    ) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(componentActivity) { task ->
                if (task.isSuccessful) {
                    onSignInSuccess(firebaseAuth.currentUser?.toUserData() ?: UserData())
                } else {
                    onSignInFailure(task.exception?.localizedMessage ?: componentActivity.getString(R.string.sign_in_failed))
                }
            }
    }
}
