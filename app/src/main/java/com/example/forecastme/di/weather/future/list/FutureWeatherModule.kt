package com.example.forecastme.di.weather.future.list

import com.example.forecastme.domain.weather.future.list.FutureWeatherInteractor
import com.example.forecastme.domain.weather.future.list.FutureWeatherInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class FutureWeatherModule {

    @Binds
    @ViewModelScoped
    abstract fun bindFutureWeatherInteractor(impl: FutureWeatherInteractorImpl): FutureWeatherInteractor
}