package com.moonlightbutterfly.cryptohub.data.signin

import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.data.signin.FirebaseSignInHandler
import com.moonlightbutterfly.cryptohub.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeFirebaseSignInHandler : FirebaseSignInHandler {
    override fun signIn(): Flow<Result<User>> {
        return MutableSharedFlow()
    }
}
