package com.moonlightbutterfly.cryptohub.signin

import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class FakeFirebaseSignInHandler @Inject constructor() : FirebaseSignInHandler {
    override fun signIn(): Flow<Answer<User>> {
        return MutableSharedFlow()
    }
}
