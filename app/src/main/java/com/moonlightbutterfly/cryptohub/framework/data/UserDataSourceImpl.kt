package com.moonlightbutterfly.cryptohub.framework.data

import com.moonlightbutterfly.cryptohub.data.Error
import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.data.UserDataSource
import com.moonlightbutterfly.cryptohub.models.User

object UserDataSourceImpl : UserDataSource {

    private var user: User? = null

    override fun getUser(): Result<User> {
        return if (user == null) {
            Result.Failure(Error.NotFound("User not found"))
        } else {
            Result.Success(user!!)
        }
    }

    override fun signIn(user: User): Result<Unit> {
        UserDataSourceImpl.user = user
        return Result.Success(Unit)
    }

    override fun signOut(): Result<Unit> {
        user = null
        return Result.Success(Unit)
    }

    override fun isUserSignedIn(): Result<Boolean> {
        return Result.Success(user != null)
    }
}
