package com.moonlightbutterfly.cryptohub.data

import com.moonlightbutterfly.cryptohub.models.UserData

/**
 * The interface describing a functionality of saving and removing the user from local memory
 * when he signs in or signs out from the application.
 */
interface UserDataCache {
    fun signInUser(userData: UserData): Result<Unit>
    fun signOutUser(): Result<Unit>
    fun getUserData(): Result<UserData>
    fun isUserSignedIn(): Result<Boolean>
}
