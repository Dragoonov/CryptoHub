package com.moonlightbutterfly.cryptohub.di

import com.moonlightbutterfly.cryptohub.domain.models.UserData
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UserDataModule {

    @Provides
    @Singleton
    fun provideUserData(): UserData = UserData()
}
