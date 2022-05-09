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
import com.moonlightbutterfly.cryptohub.domain.models.UserData
import com.moonlightbutterfly.cryptohub.utils.toUserData

/**
 * Controller that's responsible for the facebook sign in flow.
 * @param hostActivity Activity that hosts the flow
 * @param loginManager [LoginManager] instance that manages the flow at Facebook side
 * @param firebaseAuth [FirebaseAuth] instance that manages the flow at Google side
 */
class FacebookSignInController(
    private val hostActivity: ComponentActivity,
    private val loginManager: LoginManager,
    private val firebaseAuth: FirebaseAuth
) {

    /**
     * Launches sign in flow.
     * @param onSignInSuccess Callback to invoke if sign in flow ends successfully
     * @param onSignInFailure Callback to invoke if sign in flow ends with failure
     */
    fun signIn(
        onSignInSuccess: (user: UserData) -> Unit,
        onSignInFailure: (message: String) -> Unit
    ) {
        val callbackManager = CallbackManager.Factory.create()
        loginManager.apply {
            registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onCancel() {
                        onSignInFailure(hostActivity.getString(R.string.sign_in_failed))
                    }

                    override fun onError(error: FacebookException) {
                        onSignInFailure(
                            error.localizedMessage ?: hostActivity.getString(R.string.sign_in_failed)
                        )
                    }

                    override fun onSuccess(result: LoginResult) {
                        handleFacebookAccessToken(result.accessToken, onSignInSuccess, onSignInFailure)
                    }
                }
            )
            logInWithReadPermissions(
                hostActivity,
                callbackManager,
                listOf("public_profile", "email")
            )
        }
    }

    private fun handleFacebookAccessToken(
        token: AccessToken,
        onSignInSuccess: (user: UserData) -> Unit,
        onSignInFailure: (message: String) -> Unit
    ) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(hostActivity) { task ->
                if (task.isSuccessful) {
                    onSignInSuccess(firebaseAuth.currentUser?.toUserData() ?: UserData())
                } else {
                    onSignInFailure(task.exception?.localizedMessage ?: hostActivity.getString(R.string.sign_in_failed))
                }
            }
    }
}
