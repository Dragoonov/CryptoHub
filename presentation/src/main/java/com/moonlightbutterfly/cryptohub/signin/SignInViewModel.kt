package com.moonlightbutterfly.cryptohub.signin

import com.moonlightbutterfly.cryptohub.core.BaseViewModel
import com.moonlightbutterfly.cryptohub.data.common.getOrNull
import com.moonlightbutterfly.cryptohub.data.common.unpack
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import com.moonlightbutterfly.cryptohub.usecases.GetLocalPreferencesUseCase
import com.moonlightbutterfly.cryptohub.usecases.IsUserSignedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInFacade: SignInFacade,
    private val isUserSignedInUseCase: IsUserSignedInUseCase,
    private val getLocalPreferencesUseCase: GetLocalPreferencesUseCase,
    initialState: SignInUIState
) : BaseViewModel<SignInIntent, SignInUIState>(initialState) {

    init {
        acceptIntent(SignInIntent.GetData)
    }

    private fun signIn(): Flow<SignInUIState> = flow {
        signInFacade.signIn().first().getOrNull()?.let {
            emit(uiState.value.copy(isUserSignedIn = true))
        }
    }
    private fun getData(): Flow<SignInUIState> = flow {
        emit(
            SignInUIState(
                nightModeEnabled = getLocalPreferencesUseCase()
                    .first()
                    .unpack(LocalPreferences.DEFAULT)
                    .nightModeEnabled,
                isUserSignedIn = isUserSignedInUseCase().unpack(false),
                isLoading = false,
                error = null
            )
        )
    }
    override fun mapIntent(intent: SignInIntent): Flow<SignInUIState> {
        return when (intent) {
            is SignInIntent.GetData -> getData()
            is SignInIntent.SignIn -> signIn()
        }
    }
}
