package com.moonlightbutterfly.cryptohub.presentation

import com.moonlightbutterfly.cryptohub.usecases.EmailSignInUseCase
import com.moonlightbutterfly.cryptohub.usecases.FacebookSignInUseCase
import com.moonlightbutterfly.cryptohub.usecases.GoogleSignInUseCase
import com.moonlightbutterfly.cryptohub.usecases.PhoneSignInUseCase
import com.moonlightbutterfly.cryptohub.usecases.TwitterSignInUseCase
import javax.inject.Inject

/**
 * Class created to "consume" all the domain logic sign in use cases with the awareness of implementation detail of FirebaseUI
 * that's responsible for handling all sign in methods specified in domain layer.
 */
class SignInFacade @Inject constructor(
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val emailSignInUseCase: EmailSignInUseCase,
    private val phoneSignInUseCase: PhoneSignInUseCase,
    private val facebookSignInUseCase: FacebookSignInUseCase,
    private val twitterSignInUseCase: TwitterSignInUseCase,
) {
    fun signIn() = googleSignInUseCase() // Invoke Google use case, but any use case lead to FirebaseUI flow that handles all the remaining ones.
}
