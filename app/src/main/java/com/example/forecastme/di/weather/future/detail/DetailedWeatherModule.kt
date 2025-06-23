package com.example.forecastme.di.weather.future.detail

import com.example.forecastme.domain.weather.future.detail.DetailedWeatherInteractor
import com.example.forecastme.domain.weather.future.detail.DetailedWeatherInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class DetailedWeatherModule {

    @Binds
    @ViewModelScoped
    abstract fun bindDetailedWeatherInteractor(impl: DetailedWeatherInteractorImpl): DetailedWeatherInteractor
}