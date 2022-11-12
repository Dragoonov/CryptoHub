package com.moonlightbutterfly.cryptohub.data

import com.moonlightbutterfly.cryptohub.data.signin.AnonymousSignInHandler
import com.moonlightbutterfly.cryptohub.data.signin.EmailSignInHandler
import com.moonlightbutterfly.cryptohub.data.signin.FacebookSignInHandler
import com.moonlightbutterfly.cryptohub.data.signin.GoogleSignInHandler
import com.moonlightbutterfly.cryptohub.data.signin.PhoneSignInHandler
import com.moonlightbutterfly.cryptohub.data.signin.TwitterSignInHandler
import com.moonlightbutterfly.cryptohub.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class UserRepository(
    private val userDataSource: UserDataSource,
    private val googleSignInHandler: GoogleSignInHandler,
    private val facebookSignInHandler: FacebookSignInHandler,
    private val twitterSignInHandler: TwitterSignInHandler,
    private val emailSignInHandler: EmailSignInHandler,
    private val phoneSignInHandler: PhoneSignInHandler,
    private val anonymousSignInHandler: AnonymousSignInHandler
) {

    fun googleSignIn() = googleSignInHandler.signIn().saveUser()
    fun facebookSignIn() = facebookSignInHandler.signIn().saveUser()
    fun twitterSignIn() = twitterSignInHandler.signIn().saveUser()
    fun emailSignIn(email: String, password: String) = emailSignInHandler.signIn(email, password).saveUser()
    fun phoneSignIn() = phoneSignInHandler.signIn().saveUser()
    fun anonymousSignIn() = anonymousSignInHandler.signIn()
    fun signOut() = userDataSource.signOut()
    fun isUserSignedIn() = userDataSource.isUserSignedIn()
    fun getUser() = userDataSource.getUser()

    private fun Flow<Result<User>>.saveUser(): Flow<Result<User>> {
        return this.onEach { it.getOrNull()?.let { user -> userDataSource.signIn(user) } }
    }
}
