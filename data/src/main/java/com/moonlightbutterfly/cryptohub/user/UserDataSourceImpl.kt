package com.moonlightbutterfly.cryptohub.user

import com.google.firebase.auth.FirebaseAuth
import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.data.common.Error
import com.moonlightbutterfly.cryptohub.data.user.UserDataSource
import com.moonlightbutterfly.cryptohub.models.User
import com.moonlightbutterfly.cryptohub.signin.FirebaseSignInHandler
import com.moonlightbutterfly.cryptohub.toUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class UserDataSourceImpl @Inject constructor(
    private val firebaseSignInHandler: FirebaseSignInHandler,
    private val firebaseAuth: FirebaseAuth
) : UserDataSource {

    override fun getUser(): Answer<User> {
        val user = firebaseAuth.currentUser?.toUser()
        return if (user == null) {
            Answer.Failure(Error.NotFound("User not found"))
        } else {
            Answer.Success(user)
        }
    }

    override fun googleSignIn(): Flow<Answer<User>> = firebaseSignInHandler.signIn()

    override fun facebookSignIn(): Flow<Answer<User>> = firebaseSignInHandler.signIn()

    override fun twitterSignIn(): Flow<Answer<User>> = firebaseSignInHandler.signIn()

    override fun emailSignIn(): Flow<Answer<User>> = firebaseSignInHandler.signIn()

    override fun phoneSignIn(): Flow<Answer<User>> = firebaseSignInHandler.signIn()

    override fun signOut(): Answer<Unit> {
        firebaseAuth.signOut()
        return Answer.Success(Unit)
    }

    override fun isUserSignedIn(): Answer<Boolean> {
        return Answer.Success(firebaseAuth.currentUser != null)
    }
}
