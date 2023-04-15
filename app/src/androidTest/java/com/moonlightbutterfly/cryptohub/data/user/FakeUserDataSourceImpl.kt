package com.moonlightbutterfly.cryptohub.data.user

import com.moonlightbutterfly.cryptohub.data.common.Error
import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeUserDataSourceImpl @Inject constructor() : UserDataSource {

    override fun getUser(): Answer<User> {
        return Answer.Failure(Error.NotFound("User not found"))
    }

    override fun googleSignIn(): Flow<Answer<User>> = flowOf()

    override fun facebookSignIn(): Flow<Answer<User>> = flowOf()

    override fun twitterSignIn(): Flow<Answer<User>> = flowOf()

    override fun emailSignIn(): Flow<Answer<User>> = flowOf()

    override fun phoneSignIn(): Flow<Answer<User>> = flowOf()

    override fun signOut(): Answer<Unit> {
        return Answer.Success(Unit)
    }

    override fun isUserSignedIn(): Answer<Boolean> {
        return Answer.Success(false)
    }
}
