package com.example.forecastme.domain.weather.future.list

import com.example.forecastme.domain.weather.future.list.model.FutureWeather
import com.example.forecastme.domain.weather.model.WeatherState
import kotlinx.coroutines.flow.Flow

interface FutureWeatherInteractor {
    fun observeFutureWeather(): Flow<WeatherState<List<FutureWeather>>>
    suspend fun refreshFutureWeather()
}