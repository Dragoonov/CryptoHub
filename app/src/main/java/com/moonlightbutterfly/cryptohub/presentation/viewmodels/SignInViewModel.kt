package com.moonlightbutterfly.cryptohub.presentation.viewmodels

import androidx.lifecycle.asLiveData
import com.moonlightbutterfly.cryptohub.data.unpack
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import com.moonlightbutterfly.cryptohub.usecases.EmailSignInUseCase
import com.moonlightbutterfly.cryptohub.usecases.FacebookSignInUseCase
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import com.moonlightbutterfly.cryptohub.usecases.GoogleSignInUseCase
import com.moonlightbutterfly.cryptohub.usecases.PhoneSignInUseCase
import com.moonlightbutterfly.cryptohub.usecases.TwitterSignInUseCase
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    getLocalPreferencesUseCase: GetLocalPreferencesUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val emailSignInUseCase: EmailSignInUseCase,
    private val phoneSignInUseCase: PhoneSignInUseCase,
    private val facebookSignInUseCase: FacebookSignInUseCase,
    private val twitterSignInUseCase: TwitterSignInUseCase

) : BaseViewModel() {

    val isNightModeEnabled = getLocalPreferencesUseCase().propagateErrors()
        .map { it.unpack(LocalPreferences.DEFAULT).nightModeEnabled }.asLiveData()

    fun signInThroughGoogle() = googleSignInUseCase().propagateErrors()

    fun signInThroughEmail(email: String, password: String) = emailSignInUseCase(email, password).propagateErrors()

    fun signInThroughPhone() = phoneSignInUseCase().propagateErrors()

    fun signInThroughFacebook() = facebookSignInUseCase().propagateErrors()

    fun signInThroughTwitter() = twitterSignInUseCase().propagateErrors()
}
