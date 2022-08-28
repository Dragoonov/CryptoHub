package com.moonlightbutterfly.cryptohub.di

import android.content.Context
import com.moonlightbutterfly.cryptohub.presentation.ViewModelFactory
import com.moonlightbutterfly.cryptohub.signincontrollers.GoogleSignInIntentController
import dagger.BindsInstance
import dagger.Component

@Component(modules = [TestRepositoryModule::class, UseCasesModule::class, SignInModule::class])
@ActivityScope
interface TestAppComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
        ): TestAppComponent
    }

    fun viewModelFactory(): ViewModelFactory
    fun googleSignInIntentController(): GoogleSignInIntentController
}
