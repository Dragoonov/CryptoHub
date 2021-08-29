package com.moonlightbutterfly.cryptohub.di

import android.content.Context
import com.moonlightbutterfly.cryptohub.viewmodels.ViewModelFactory
import dagger.BindsInstance
import dagger.Component

@Component(modules = [RepositoryModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance context: Context): AppComponent
    }

    fun viewModelFactory(): ViewModelFactory
}
