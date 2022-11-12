package com.moonlightbutterfly.cryptohub.di

import android.content.Context
import androidx.activity.ComponentActivity
import dagger.Module
import dagger.Provides

@Module
class SignInModule {

    @Provides
    fun provideComponentActivity(context: Context): ComponentActivity {
        return context as ComponentActivity
    }
}
