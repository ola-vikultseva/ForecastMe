package com.example.forecastme.domain

import com.example.forecastme.domain.model.Location
import com.example.forecastme.data.model.ResultWrapper
import com.example.forecastme.data.db.entity.current.CurrentWeatherEntity
import com.example.forecastme.data.db.entity.future.FutureWeatherWithHourly

interface WeatherRepository {

    suspend fun getCurrentWeather(
        location: Location,
        isRefresh: Boolean
    ): ResultWrapper<CurrentWeatherEntity>

    suspend fun getFutureWeather(
        daysCount: Int,
        location: Location,
        isRefresh: Boolean
    ): ResultWrapper<List<FutureWeatherWithHourly>>
}