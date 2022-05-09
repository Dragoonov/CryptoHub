package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.UserDataCache

class GetSignedInUserUseCase(private val userDataCache: UserDataCache) {
    operator fun invoke() = userDataCache.getUserData()
}
