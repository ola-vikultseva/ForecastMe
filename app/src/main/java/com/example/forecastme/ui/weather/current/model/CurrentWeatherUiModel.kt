package com.example.forecastme.ui.weather.current.model

data class CurrentWeatherUiModel(
    val cityName: String,
    val description: String,
    val temperature: String,
    val feelsLike: String,
    val wind: String,
    val pressure: String,
    val humidity: String,
    val uvIndex: String,
    val iconUrl: String
)