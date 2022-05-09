package com.moonlightbutterfly.cryptohub.di

import android.content.Context
import com.moonlightbutterfly.cryptohub.presentation.ViewModelFactory
import com.moonlightbutterfly.cryptohub.signincontrollers.GoogleSignInIntentController
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [TestRepositoryModule::class, UseCasesModule::class, SignInModule::class])
@Singleton
interface TestAppComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
            @BindsInstance googleSignInIntentController: GoogleSignInIntentController
        ): TestAppComponent
    }

    fun viewModelFactory(): ViewModelFactory
}
