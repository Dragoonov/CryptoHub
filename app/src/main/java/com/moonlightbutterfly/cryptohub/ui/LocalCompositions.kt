package com.moonlightbutterfly.cryptohub.ui

import androidx.compose.runtime.compositionLocalOf
import com.moonlightbutterfly.cryptohub.viewmodels.ViewModelFactory

val LocalViewModelFactory = compositionLocalOf<ViewModelFactory> { error("No view model factory found!") }
