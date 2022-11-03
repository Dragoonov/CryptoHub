package com.moonlightbutterfly.cryptohub.framework

import com.moonlightbutterfly.cryptohub.data.Error
import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.data.UserDataCache
import com.moonlightbutterfly.cryptohub.models.UserData

object UserDataCacheImpl : UserDataCache {

    private var userData: UserData? = null

    override fun signInUser(userData: UserData): Result<Unit> {
        this.userData = userData
        return Result.Success(Unit)
    }

    override fun signOutUser(): Result<Unit> {
        this.userData = null
        return Result.Success(Unit)
    }

    override fun getUserData(): Result<UserData> {
        return if (this.userData == null) {
            Result.Failure(Error.NotFound("User not found"))
        } else {
            Result.Success(this.userData!!)
        }
    }

    override fun isUserSignedIn(): Result<Boolean> {
        return Result.Success(this.userData != null)
    }
}
