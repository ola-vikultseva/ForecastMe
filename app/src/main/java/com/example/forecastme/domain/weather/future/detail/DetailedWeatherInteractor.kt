package com.example.forecastme.domain.weather.future.detail

import com.example.forecastme.domain.weather.future.detail.model.DetailedWeather

interface DetailedWeatherInteractor {
    suspend fun getDetailedWeather(dayOfWeek: Int): DetailedWeather
}
