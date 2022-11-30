package com.moonlightbutterfly.cryptohub.usecases

import com.moonlightbutterfly.cryptohub.data.user.UserRepository

class GoogleSignInUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.googleSignIn()
}

class EmailSignInUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.emailSignIn()
}

class FacebookSignInUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.facebookSignIn()
}

class PhoneSignInUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.phoneSignIn()
}

class TwitterSignInUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.twitterSignIn()
}

class SignOutUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.signOut()
}

class GetSignedInUserUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.getUser()
}

class IsUserSignedInUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.isUserSignedIn()
}
