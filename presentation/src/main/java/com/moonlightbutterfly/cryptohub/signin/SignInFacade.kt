package com.moonlightbutterfly.cryptohub.signin

import com.moonlightbutterfly.cryptohub.usecases.GoogleSignInUseCase
import javax.inject.Inject

/**
 * Class created to "consume" all the domain logic sign in use cases with the awareness of
 * implementation detail of FirebaseUI that's responsible for handling all sign in methods
 * specified in domain layer.
 */
class SignInFacade @Inject constructor(
    private val googleSignInUseCase: GoogleSignInUseCase,
//    private val emailSignInUseCase: EmailSignInUseCase,
//    private val phoneSignInUseCase: PhoneSignInUseCase,
//    private val facebookSignInUseCase: FacebookSignInUseCase,
//    private val twitterSignInUseCase: TwitterSignInUseCase,
) {
    // Invoke Google use case, but any use case lead to FirebaseUI flow
    // that handles all the remaining ones.
    fun signIn() = googleSignInUseCase()
}
