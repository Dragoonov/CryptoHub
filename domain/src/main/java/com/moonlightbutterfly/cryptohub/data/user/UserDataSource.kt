package com.moonlightbutterfly.cryptohub.data.user

import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.models.User
import kotlinx.coroutines.flow.Flow

interface UserDataSource {

    fun getUser(): Answer<User>

    fun googleSignIn(): Flow<Answer<User>>

    fun facebookSignIn(): Flow<Answer<User>>

    fun twitterSignIn(): Flow<Answer<User>>

    fun emailSignIn(): Flow<Answer<User>>

    fun phoneSignIn(): Flow<Answer<User>>

    fun signOut(): Answer<Unit>

    fun isUserSignedIn(): Answer<Boolean>
}
