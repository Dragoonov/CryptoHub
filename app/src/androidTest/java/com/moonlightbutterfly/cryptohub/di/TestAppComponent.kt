package com.moonlightbutterfly.cryptohub.di

import dagger.Component

@Component(modules = [TestRepositoryModule::class])
interface TestAppComponent : AppComponent
