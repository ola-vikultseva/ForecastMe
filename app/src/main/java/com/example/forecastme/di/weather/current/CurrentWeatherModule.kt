package com.example.forecastme.di.weather.current

import com.example.forecastme.domain.weather.current.CurrentWeatherInteractor
import com.example.forecastme.domain.weather.current.CurrentWeatherInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class CurrentWeatherModule {

    @Binds
    @ViewModelScoped
    abstract fun bindCurrentWeatherInteractor(impl: CurrentWeatherInteractorImpl): CurrentWeatherInteractor
}