package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserDataCache
import com.moonlightbutterfly.cryptohub.domain.models.UserData

class SignInUserUseCase(private val userDataCache: UserDataCache) {
    operator fun invoke(userData: UserData) = userDataCache.signInUser(userData)
}
