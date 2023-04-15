package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.models.User
import kotlinx.coroutines.flow.Flow

fun interface GoogleSignInUseCase: () -> Flow<Answer<User>>
fun interface EmailSignInUseCase: () -> Flow<Answer<User>>
fun interface FacebookSignInUseCase: () -> Flow<Answer<User>>
fun interface PhoneSignInUseCase: () -> Flow<Answer<User>>
fun interface TwitterSignInUseCase: () -> Flow<Answer<User>>
fun interface SignOutUseCase: () -> Answer<Unit>
fun interface GetSignedInUserUseCase: () -> Answer<User>
fun interface IsUserSignedInUseCase: () -> Answer<Boolean>
