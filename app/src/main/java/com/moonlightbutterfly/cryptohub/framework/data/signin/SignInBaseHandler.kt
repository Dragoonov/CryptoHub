package com.moonlightbutterfly.cryptohub.framework.data.signin

import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.models.User
import kotlinx.coroutines.channels.Channel

abstract class SignInBaseHandler {
    protected val signInChannel = Channel<Result<User>>()
}