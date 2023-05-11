package com.moonlightbutterfly.cryptohub.signin

import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.models.User
import kotlinx.coroutines.flow.Flow

/**
 * Interface scoping the implementation of Firebase sign in mechanisms and unifying it to single
 * sign in method.
 */
internal interface FirebaseSignInHandler {
    fun signIn(): Flow<Answer<User>>
}
