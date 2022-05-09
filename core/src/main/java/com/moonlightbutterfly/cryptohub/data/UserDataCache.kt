package com.moonlightbutterfly.cryptohub.data

import com.moonlightbutterfly.cryptohub.domain.models.UserData

/**
 * The interface describing a functionality of saving and removing the user from local memory
 * when he signs in or signs out from the application.
 */
interface UserDataCache {
    fun signInUser(userData: UserData)
    fun signOutUser()
    fun getUserData(): UserData?
}
