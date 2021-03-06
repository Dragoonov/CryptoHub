package com.moonlightbutterfly.cryptohub.signincontrollers

import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.domain.models.UserData
import com.moonlightbutterfly.cryptohub.utils.toUserData

/**
 * Controller that's responsible for the email sign in flow.
 * @param hostActivity Activity that hosts the flow
 * @param firebaseAuth [FirebaseAuth] instance that manages the flow
 */
class EmailSignInController(
    private val hostActivity: ComponentActivity,
    private val firebaseAuth: FirebaseAuth
) {

    /**
     * Launches sign in flow.
     * @param email Provided email
     * @param password Provided password
     * @param onSignInSuccess Callback to invoke if sign in flow ends successfully
     * @param onSignInFailure Callback to invoke if sign in flow ends with failure
     */
    fun signIn(
        email: String,
        password: String,
        onSignInSuccess: (user: UserData) -> Unit,
        onSignInFailure: (message: String) -> Unit
    ) {
        if (email.isEmpty() or password.isEmpty()) {
            onSignInFailure(hostActivity.getString(R.string.fields_not_empty))
            return
        }
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(hostActivity) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser?.toUserData() ?: UserData()
                    onSignInSuccess(user)
                } else {
                    onSignInFailure(
                        task.exception?.message ?: hostActivity.getString(R.string.sign_in_failed)
                    )
                }
            }
    }
}
