package com.moonlightbutterfly.cryptohub.di

import android.content.Context
import com.moonlightbutterfly.cryptohub.presentation.ViewModelFactory
import com.moonlightbutterfly.cryptohub.signincontrollers.GoogleSignInIntentController
import dagger.BindsInstance
import dagger.Component

@Component(modules = [UseCasesModule::class, RepositoryModule::class, SignInModule::class])
@ActivityScope
interface AppComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }

    fun viewModelFactory(): ViewModelFactory
    fun googleSignInIntentController(): GoogleSignInIntentController
}
