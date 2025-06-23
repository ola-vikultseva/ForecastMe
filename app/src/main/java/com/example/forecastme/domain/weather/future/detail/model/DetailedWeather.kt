package com.example.forecastme.domain.weather.future.detail.model

data class DetailedWeather(
    val timestampSec: Long,
    val description: String,
    val temperature: Double,
    val dailyTemperature: DailyTemperature,
    val windSpeed: Double,
    val windDirection: String,
    val pressure: Double,
    val humidity: Int,
    val uvIndex: Double,
    val iconUrl: String
)

data class DailyTemperature(
    val morning: Double,
    val day: Double,
    val evening: Double,
    val night: Double
)