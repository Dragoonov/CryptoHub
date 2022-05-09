package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserDataCache

class SignOutUserUseCase(private val userDataCache: UserDataCache) {
    operator fun invoke() = userDataCache.signOutUser()
}
