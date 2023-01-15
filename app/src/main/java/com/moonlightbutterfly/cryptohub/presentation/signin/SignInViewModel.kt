package com.moonlightbutterfly.cryptohub.presentation.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.moonlightbutterfly.cryptohub.data.common.getOrNull
import com.moonlightbutterfly.cryptohub.models.User
import com.moonlightbutterfly.cryptohub.presentation.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInFacade: SignInFacade
) : BaseViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun signIn() {
        viewModelScope.launch {
            signInFacade.signIn().propagateErrors().collect {
                it.getOrNull()?.let { user ->
                    _user.value = user
                }
            }
        }
    }
}
