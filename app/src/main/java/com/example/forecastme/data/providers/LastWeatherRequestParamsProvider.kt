package com.example.forecastme.data.providers

import com.example.forecastme.domain.model.Location
import com.example.forecastme.data.utils.WeatherType

interface LastWeatherRequestParamsProvider {
    fun getLocation(weatherType: WeatherType): Location?
    fun setLocation(weatherType: WeatherType, location: Location)
    fun getRequestTimeMillis(weatherType: WeatherType): Long
    fun setRequestTime(weatherType: WeatherType, requestTimeMillis: Long)
}