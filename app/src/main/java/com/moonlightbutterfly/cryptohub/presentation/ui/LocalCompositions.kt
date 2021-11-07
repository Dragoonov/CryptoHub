package com.moonlightbutterfly.cryptohub.presentation.ui

import androidx.compose.runtime.compositionLocalOf
import com.moonlightbutterfly.cryptohub.presentation.ViewModelFactory

val LocalViewModelFactory = compositionLocalOf<ViewModelFactory> { error("No view model factory found!") }
