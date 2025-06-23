package com.example.forecastme.domain.weather.current

import com.example.forecastme.domain.weather.model.WeatherState
import com.example.forecastme.domain.weather.current.model.CurrentWeather
import kotlinx.coroutines.flow.Flow

interface CurrentWeatherInteractor {
    fun observeCurrentWeather(): Flow<WeatherState<CurrentWeather>>
    suspend fun refreshCurrentWeather()
}