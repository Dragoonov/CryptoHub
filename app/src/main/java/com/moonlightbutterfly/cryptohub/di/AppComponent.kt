package com.moonlightbutterfly.cryptohub.di

import android.content.Context
import com.moonlightbutterfly.cryptohub.presentation.ViewModelFactory
import com.moonlightbutterfly.cryptohub.signincontrollers.GoogleSignInIntentController
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [UseCasesModule::class, RepositoryModule::class, SignInModule::class])
@Singleton
interface AppComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
            @BindsInstance googleSignInIntentController: GoogleSignInIntentController
        ): AppComponent
    }

    fun viewModelFactory(): ViewModelFactory
}
