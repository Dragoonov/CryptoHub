package com.moonlightbutterfly.cryptohub.framework.data

import com.google.firebase.auth.FirebaseAuth
import com.moonlightbutterfly.cryptohub.data.Error
import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.data.UserDataSource
import com.moonlightbutterfly.cryptohub.framework.data.signin.FirebaseSignInHandler
import com.moonlightbutterfly.cryptohub.models.User
import com.moonlightbutterfly.cryptohub.utils.toUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val firebaseSignInHandler: FirebaseSignInHandler,
    private val firebaseAuth: FirebaseAuth
) : UserDataSource {

    override fun getUser(): Result<User> {
        val user = firebaseAuth.currentUser?.toUser()
        return if (user == null) {
            Result.Failure(Error.NotFound("User not found"))
        } else {
            Result.Success(user)
        }
    }

    override fun googleSignIn(): Flow<Result<User>> = firebaseSignInHandler.signIn()

    override fun facebookSignIn(): Flow<Result<User>> = firebaseSignInHandler.signIn()

    override fun twitterSignIn(): Flow<Result<User>> = firebaseSignInHandler.signIn()

    override fun emailSignIn(): Flow<Result<User>> = firebaseSignInHandler.signIn()

    override fun phoneSignIn(): Flow<Result<User>> = firebaseSignInHandler.signIn()

    override fun signOut(): Result<Unit> {
        firebaseAuth.signOut()
        return Result.Success(Unit)
    }

    override fun isUserSignedIn(): Result<Boolean> {
        return Result.Success(firebaseAuth.currentUser != null)
    }
}
