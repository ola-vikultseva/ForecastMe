package com.example.forecastme.ui.weather.future.list.model

data class FutureWeatherUiModel(
    val cityName: String,
    val data: List<FutureWeatherItem>
)

data class FutureWeatherItem(
    val date: String,
    val dayOfWeekInt: Int,
    val dayOfWeekName: String,
    val temperature: String,
    val description: String,
    val iconUrl: String
)