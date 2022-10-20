package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserDataCache
import com.moonlightbutterfly.cryptohub.models.UserData

class SignInUserUseCase(private val userDataCache: UserDataCache) {
    operator fun invoke(userData: UserData) = userDataCache.signInUser(userData)
}

class SignOutUserUseCase(private val userDataCache: UserDataCache) {
    operator fun invoke() = userDataCache.signOutUser()
}

class GetSignedInUserUseCase(private val userDataCache: UserDataCache) {
    operator fun invoke() = userDataCache.getUserData()
}

class IsUserSignedInUseCase(private val userDataCache: UserDataCache) {
    operator fun invoke() = userDataCache.isUserSignedIn()
}
