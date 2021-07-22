package com.moonlightbutterfly.cryptohub.di

import dagger.Component
import javax.inject.Singleton

@Component(modules = [TestRepositoryModule::class])
interface TestAppComponent: AppComponent