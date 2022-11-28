package com.moonlightbutterfly.cryptohub.framework.data.signin

import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.models.User
import kotlinx.coroutines.flow.Flow

/**
 * Interface scoping the implementation of Firebase sign in mechanisms and unifying it to single
 * sign in method.
 */
interface FirebaseSignInHandler {
    fun signIn(): Flow<Result<User>>
}