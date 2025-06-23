package com.example.forecastme.domain.weather.model

import com.example.forecastme.data.utils.ErrorType
import com.example.forecastme.domain.model.Location

sealed class WeatherState<T> {
    class Loading<T> : WeatherState<T>()
    data class Data<T>(val data: T, val location: Location) : WeatherState<T>()
    data class Error<T>(val error: ErrorType) : WeatherState<T>()
}