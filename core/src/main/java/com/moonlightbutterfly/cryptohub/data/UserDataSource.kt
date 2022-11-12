package com.moonlightbutterfly.cryptohub.data

import com.moonlightbutterfly.cryptohub.models.User

interface UserDataSource {

    fun getUser(): Result<User>

    fun signIn(user: User): Result<Unit>

    fun signOut(): Result<Unit>

    fun isUserSignedIn(): Result<Boolean>
}