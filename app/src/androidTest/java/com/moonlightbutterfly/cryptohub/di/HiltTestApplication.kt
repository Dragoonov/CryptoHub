package com.moonlightbutterfly.cryptohub.di

import com.moonlightbutterfly.cryptohub.presentation.core.BaseApplication
import dagger.hilt.android.testing.CustomTestApplication

@CustomTestApplication(BaseApplication::class)
interface HiltTestApplication
