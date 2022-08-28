package com.moonlightbutterfly.cryptohub.framework

import com.moonlightbutterfly.cryptohub.data.UserDataCache
import com.moonlightbutterfly.cryptohub.domain.models.UserData

object UserDataCacheImpl : UserDataCache {

    private var userData: UserData? = null

    override fun signInUser(userData: UserData) {
        this.userData = userData
    }

    override fun signOutUser() {
        this.userData = null
    }

    override fun getUserData(): UserData? {
        return this.userData
    }
}
