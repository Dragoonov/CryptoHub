package com.moonlightbutterfly.cryptohub.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moonlightbutterfly.cryptohub.data.common.getOrNull
import com.moonlightbutterfly.cryptohub.data.common.unpack
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import com.moonlightbutterfly.cryptohub.models.User
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import com.moonlightbutterfly.cryptohub.usecases.IsUserSignedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInFacade: SignInFacade,
    private val isUserSignedInUseCase: IsUserSignedInUseCase,
    getLocalPreferencesUseCase: GetLocalPreferencesUseCase
) : ViewModel() {

    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    val isNightModeEnabled = getLocalPreferencesUseCase()
        .map { it.unpack(LocalPreferences.DEFAULT).nightModeEnabled }

    fun signIn() {
        viewModelScope.launch {
            signInFacade.signIn()
                .onEach { answer -> answer.getOrNull()?.let { _user.value = it } }
                .collect()
        }
    }
    fun isUserSignedIn() = runBlocking { isUserSignedInUseCase().unpack(false) }
}
