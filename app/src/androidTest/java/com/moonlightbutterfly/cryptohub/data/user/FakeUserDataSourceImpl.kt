package com.moonlightbutterfly.cryptohub.data.user

import com.moonlightbutterfly.cryptohub.data.common.Error
import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeUserDataSourceImpl @Inject constructor(): UserDataSource {

    override fun getUser(): Result<User> {
        return Result.Failure(Error.NotFound("User not found"))
    }

    override fun googleSignIn(): Flow<Result<User>> = flowOf()

    override fun facebookSignIn(): Flow<Result<User>> = flowOf()

    override fun twitterSignIn(): Flow<Result<User>> = flowOf()

    override fun emailSignIn(): Flow<Result<User>> = flowOf()

    override fun phoneSignIn(): Flow<Result<User>> = flowOf()

    override fun signOut(): Result<Unit> {
        return Result.Success(Unit)
    }

    override fun isUserSignedIn(): Result<Boolean> {
        return Result.Success(false)
    }
}