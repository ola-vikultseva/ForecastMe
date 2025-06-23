package com.example.forecastme.di.settings

import com.example.forecastme.domain.settings.SettingsInteractor
import com.example.forecastme.domain.settings.SettingsInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class SettingsModule {

    @Binds
    @ViewModelScoped
    abstract fun bindSettingsInteractor(impl: SettingsInteractorImpl): SettingsInteractor
}