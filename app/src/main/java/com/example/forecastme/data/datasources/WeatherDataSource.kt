package com.example.forecastme.data.datasources

import com.example.forecastme.data.model.ResultWrapper
import com.example.forecastme.data.network.response.CurrentWeatherResponse
import com.example.forecastme.data.network.response.FutureWeatherResponse

interface WeatherDataSource {

    suspend fun fetchCurrentWeather(
        cityName: String
    ): ResultWrapper<CurrentWeatherResponse>

    suspend fun fetchFutureWeather(
        days: Int,
        cityName: String
    ): ResultWrapper<FutureWeatherResponse>
}