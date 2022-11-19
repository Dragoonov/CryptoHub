package com.moonlightbutterfly.cryptohub.data

class UserRepository(
    private val userDataSource: UserDataSource,
) {

    fun googleSignIn() = userDataSource.googleSignIn()
    fun facebookSignIn() = userDataSource.facebookSignIn()
    fun twitterSignIn() = userDataSource.twitterSignIn()
    fun emailSignIn() = userDataSource.emailSignIn()
    fun phoneSignIn() = userDataSource.phoneSignIn()
    fun signOut() = userDataSource.signOut()
    fun isUserSignedIn() = userDataSource.isUserSignedIn()
    fun getUser() = userDataSource.getUser()
}
