package com.moonlightbutterfly.cryptohub.framework.data.signin

import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.models.User
import kotlinx.coroutines.flow.Flow

interface FirebaseSignInHandler {
    fun signIn(): Flow<Result<User>>
}