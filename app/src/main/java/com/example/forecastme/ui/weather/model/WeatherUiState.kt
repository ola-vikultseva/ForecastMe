package com.example.forecastme.ui.weather.model

sealed class WeatherUiState<T> {
    class Loading<T> : WeatherUiState<T>()
    data class Data<T>(val data: T) : WeatherUiState<T>()
    data class Error<T>(val errorMessage: String) : WeatherUiState<T>()
}