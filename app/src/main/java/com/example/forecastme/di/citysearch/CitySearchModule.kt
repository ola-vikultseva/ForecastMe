package com.example.forecastme.di.citysearch

import com.example.forecastme.domain.citysearch.CitySearchInteractor
import com.example.forecastme.domain.citysearch.CitySearchInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class CitySearchModule {

    @Binds
    @ViewModelScoped
    abstract fun bindCitySearchInteractor(impl: CitySearchInteractorImpl): CitySearchInteractor
}