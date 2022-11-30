package com.moonlightbutterfly.cryptohub.data.user

import com.moonlightbutterfly.cryptohub.data.common.Result
import com.moonlightbutterfly.cryptohub.models.User
import kotlinx.coroutines.flow.Flow

interface UserDataSource {

    fun getUser(): Result<User>

    fun googleSignIn(): Flow<Result<User>>

    fun facebookSignIn(): Flow<Result<User>>

    fun twitterSignIn(): Flow<Result<User>>

    fun emailSignIn(): Flow<Result<User>>

    fun phoneSignIn(): Flow<Result<User>>

    fun signOut(): Result<Unit>

    fun isUserSignedIn(): Result<Boolean>
}
