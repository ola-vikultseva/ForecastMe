package com.example.forecastme.domain.weather.future.list.model

data class FutureWeather(
    val dayOfWeekInt: Int,
    val timestampSec: Long,
    val description: String,
    val temperature: Double,
    val iconUrl: String
)