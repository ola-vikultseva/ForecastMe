package com.example.forecastme.domain.weather.current.model

data class CurrentWeather(
    val description: String,
    val temperature: Double,
    val feelsLike: Double,
    val windSpeed: Double,
    val windDirection: String,
    val pressure: Double,
    val humidity: Int,
    val uvIndex: Double,
    val iconUrl: String
)