package com.moonlightbutterfly.cryptohub.di

import dagger.Component
import javax.inject.Singleton

@Component(modules = [TestRepositoryModule::class, UseCasesModule::class, UserDataModule::class])
@Singleton
interface TestAppComponent : AppComponent
