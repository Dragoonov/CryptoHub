package com.moonlightbutterfly.cryptohub.framework.data.signin

import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.data.signin.EmailSignInHandler
import com.moonlightbutterfly.cryptohub.models.User
import com.moonlightbutterfly.cryptohub.utils.toUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class EmailSignInHandlerImpl(
    private val firebaseAuth: FirebaseAuth,
    private val hostActivity: ComponentActivity
): SignInBaseHandler(), EmailSignInHandler {

    override fun signIn(
        email: String,
        password: String,
    ): Flow<Result<User>> {
        if (email.isEmpty() or password.isEmpty()) {
            signInChannel.trySend(Result.Failure(error("Fields cannot be empty")))
            return signInChannel.receiveAsFlow()
        }
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(hostActivity) { task ->
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
        return signInChannel.receiveAsFlow()
    }

}