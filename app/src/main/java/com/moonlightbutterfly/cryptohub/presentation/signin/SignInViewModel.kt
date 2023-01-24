package com.moonlightbutterfly.cryptohub.presentation.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.moonlightbutterfly.cryptohub.data.common.getOrNull
import com.moonlightbutterfly.cryptohub.data.common.unpack
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import com.moonlightbutterfly.cryptohub.models.User
import com.moonlightbutterfly.cryptohub.presentation.core.BaseViewModel
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import com.moonlightbutterfly.cryptohub.usecases.IsUserSignedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInFacade: SignInFacade,
    private val isUserSignedInUseCase: IsUserSignedInUseCase,
    getLocalPreferencesUseCase: GetLocalPreferencesUseCase
) : BaseViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    val isNightModeEnabled = getLocalPreferencesUseCase()
        .propagateErrors()
        .map { it.unpack(LocalPreferences.DEFAULT).nightModeEnabled }

    fun signIn() {
        viewModelScope.launch {
            signInFacade.signIn().propagateErrors().collect {
                it.getOrNull()?.let { user ->
                    _user.value = user
                }
            }
        }
    }
    fun isUserSignedIn() = runBlocking { isUserSignedInUseCase().propagateErrors().unpack(false) }
}
