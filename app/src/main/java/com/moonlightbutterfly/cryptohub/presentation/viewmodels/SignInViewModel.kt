package com.moonlightbutterfly.cryptohub.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.moonlightbutterfly.cryptohub.data.getOrNull
import com.moonlightbutterfly.cryptohub.models.User
import com.moonlightbutterfly.cryptohub.presentation.SignInFacade
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

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
