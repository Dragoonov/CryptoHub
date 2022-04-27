package com.moonlightbutterfly.cryptohub.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.moonlightbutterfly.cryptohub.domain.models.UserData
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val userData: UserData,
) : ViewModel() {

    fun saveUser(data: UserData) {
        userData.set(data)
    }

    fun clearUser() {
        userData.clear()
    }
}
