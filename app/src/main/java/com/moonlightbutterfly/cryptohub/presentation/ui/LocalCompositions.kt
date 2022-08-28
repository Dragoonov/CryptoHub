package com.moonlightbutterfly.cryptohub.presentation.ui

import androidx.compose.runtime.compositionLocalOf
import com.moonlightbutterfly.cryptohub.presentation.ViewModelFactory
import com.moonlightbutterfly.cryptohub.signincontrollers.GoogleSignInIntentController

val LocalViewModelFactory = compositionLocalOf<ViewModelFactory> { error("No view model factory found!") }
val LocalSignInIntentControllerProvider = compositionLocalOf<GoogleSignInIntentController> { error("No intent controller found!") }
