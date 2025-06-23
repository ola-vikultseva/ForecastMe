package com.example.forecastme.ui.weather.future.detail.model

data class DetailedWeatherUiModel(
    val cityName: String,
    val date: String,
    val description: String,
    val temperature: String,
    val dailyTemperature: DailyTemperature,
    val wind: String,
    val pressure: String,
    val humidity: String,
    val uvIndex: String,
    val iconUrl: String
)

data class DailyTemperature(
    val morning: String,
    val day: String,
    val evening: String,
    val night: String
)