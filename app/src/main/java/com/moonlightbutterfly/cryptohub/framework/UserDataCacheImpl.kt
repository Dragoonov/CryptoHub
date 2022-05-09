package com.moonlightbutterfly.cryptohub.framework

import com.google.firebase.auth.FirebaseAuth
import com.moonlightbutterfly.cryptohub.data.UserDataCache
import com.moonlightbutterfly.cryptohub.domain.models.UserData

class UserDataCacheImpl(private val firebaseAuth: FirebaseAuth) : UserDataCache {

    private var userData: UserData? = null

    override fun signInUser(userData: UserData) {
        this.userData = userData
    }

    override fun signOutUser() {
        this.userData = null
        firebaseAuth.signOut()
    }

    override fun getUserData(): UserData? {
        return this.userData
    }
}
