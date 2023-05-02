package com.moonlightbutterfly.cryptohub.di

import com.moonlightbutterfly.cryptohub.BaseApplication
import dagger.hilt.android.testing.CustomTestApplication

@CustomTestApplication(BaseApplication::class)
interface HiltTestApplication
