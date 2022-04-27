package com.moonlightbutterfly.cryptohub.di

import android.content.Context
import com.moonlightbutterfly.cryptohub.presentation.ViewModelFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [RepositoryModule::class, UseCasesModule::class, UserDataModule::class])
@Singleton
interface AppComponent {

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance context: Context): AppComponent
    }

    fun viewModelFactory(): ViewModelFactory
}
