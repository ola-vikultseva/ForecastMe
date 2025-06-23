package com.example.forecastme.data.datasources

import com.example.forecastme.data.utils.ErrorType
import com.example.forecastme.data.model.ResultWrapper
import com.example.forecastme.data.network.WeatherApiService
import com.example.forecastme.data.network.response.CurrentWeatherResponse
import com.example.forecastme.data.network.response.FutureWeatherResponse
import com.example.forecastme.data.utils.NoConnectivityException

class WeatherDataSourceImpl(
    private val weatherApiService: WeatherApiService
) : WeatherDataSource {

    override suspend fun fetchCurrentWeather(cityName: String): ResultWrapper<CurrentWeatherResponse> =
        try {
            ResultWrapper.Success(weatherApiService.getCurrentWeather(cityName))
        } catch (exception: Exception) {
            ResultWrapper.Error(convertToErrorType(exception))
        }

    override suspend fun fetchFutureWeather(days: Int, cityName: String): ResultWrapper<FutureWeatherResponse> =
        try {
            ResultWrapper.Success(weatherApiService.getFutureWeather(days, cityName))
        } catch (exception: Exception) {
            ResultWrapper.Error(convertToErrorType(exception))
        }

    private fun convertToErrorType(exception: Exception): ErrorType =
        when (exception) {
            is NoConnectivityException -> ErrorType.NO_INTERNET_ACCESS
            else -> ErrorType.UNKNOWN
        }
}