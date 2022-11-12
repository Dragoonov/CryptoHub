package com.moonlightbutterfly.cryptohub.data.signin

import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.models.User
import kotlinx.coroutines.flow.Flow

interface FacebookSignInHandler {
    fun signIn(): Flow<Result<User>>
}