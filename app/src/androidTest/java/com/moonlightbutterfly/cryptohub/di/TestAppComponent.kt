package com.moonlightbutterfly.cryptohub.di

import android.content.Context
import com.moonlightbutterfly.cryptohub.presentation.ViewModelFactory
import dagger.BindsInstance
import dagger.Component

@Component(modules = [TestRepositoryModule::class, UseCasesModule::class])
@ActivityScope
interface TestAppComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
        ): TestAppComponent
    }

    fun viewModelFactory(): ViewModelFactory
}
