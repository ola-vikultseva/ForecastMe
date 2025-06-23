package com.example.forecastme.data

import com.example.forecastme.data.datasources.WeatherDataSource
import com.example.forecastme.data.db.CurrentWeatherDao
import com.example.forecastme.data.db.FutureWeatherDao
import com.example.forecastme.data.db.entity.current.CurrentWeatherEntity
import com.example.forecastme.data.db.entity.future.FutureWeatherEntity
import com.example.forecastme.data.db.entity.future.FutureWeatherWithHourly
import com.example.forecastme.data.db.entity.future.HourlyWeatherEntity
import com.example.forecastme.data.model.ResultWrapper
import com.example.forecastme.data.network.response.CurrentWeatherResponse
import com.example.forecastme.data.network.response.FutureWeatherResponse
import com.example.forecastme.data.providers.LastWeatherRequestParamsProvider
import com.example.forecastme.domain.WeatherRepository
import com.example.forecastme.domain.model.Location
import com.example.forecastme.data.utils.WeatherType
import java.util.Calendar
import java.util.concurrent.TimeUnit

class WeatherRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val futureWeatherDao: FutureWeatherDao,
    private val weatherDataSource: WeatherDataSource,
    private val lastWeatherRequestParamsProvider: LastWeatherRequestParamsProvider
) : WeatherRepository {

    override suspend fun getCurrentWeather(
        location: Location,
        isRefresh: Boolean
    ): ResultWrapper<CurrentWeatherEntity> {
        val isFetchNewDataNeeded = isFetchNewDataNeeded(
            isRefresh = isRefresh,
            currentLocation = location,
            lastLocation = lastWeatherRequestParamsProvider.getLocation(WeatherType.CURRENT),
            currentTimeMillis = System.currentTimeMillis(),
            lastRequestTimeMillis = lastWeatherRequestParamsProvider.getRequestTimeMillis(WeatherType.CURRENT)
        )
        return if (isFetchNewDataNeeded) {
            fetchCurrentWeather(location)
        } else {
            ResultWrapper.Success(currentWeatherDao.getWeather())
        }
    }

    override suspend fun getFutureWeather(
        daysCount: Int,
        location: Location,
        isRefresh: Boolean
    ): ResultWrapper<List<FutureWeatherWithHourly>> {
        val isFetchNewDataNeeded = isFetchNewDataNeeded(
            isRefresh = isRefresh,
            currentLocation = location,
            lastLocation = lastWeatherRequestParamsProvider.getLocation(WeatherType.FUTURE),
            currentTimeMillis = System.currentTimeMillis(),
            lastRequestTimeMillis = lastWeatherRequestParamsProvider.getRequestTimeMillis(WeatherType.FUTURE)
        )
        return if (isFetchNewDataNeeded) {
            fetchFutureWeather(daysCount, location)
        } else {
            ResultWrapper.Success(futureWeatherDao.getFutureWeatherWithHourly())
        }
    }

    private suspend fun fetchCurrentWeather(
        location: Location
    ): ResultWrapper<CurrentWeatherEntity> =
        when (val currentWeatherResult = weatherDataSource.fetchCurrentWeather(location.cityName)) {
            is ResultWrapper.Loading -> ResultWrapper.Loading()
            is ResultWrapper.Success -> {
                updateLastWeatherRequestParams(
                    weatherType = WeatherType.CURRENT,
                    location = location,
                    requestTimeMillis = System.currentTimeMillis()
                )
                val currentWeatherEntity = convertToCurrentWeatherEntity(currentWeatherResult.data)
                currentWeatherDao.insertWeather(currentWeatherEntity)
                ResultWrapper.Success(currentWeatherEntity)
            }
            is ResultWrapper.Error -> ResultWrapper.Error(currentWeatherResult.error)
        }

    private suspend fun fetchFutureWeather(
        days: Int,
        location: Location
    ): ResultWrapper<List<FutureWeatherWithHourly>> =
        when (val futureWeatherResult = weatherDataSource.fetchFutureWeather(days, location.cityName)) {
            is ResultWrapper.Loading -> ResultWrapper.Loading()
            is ResultWrapper.Success -> {
                updateLastWeatherRequestParams(
                    weatherType = WeatherType.FUTURE,
                    location = location,
                    requestTimeMillis = System.currentTimeMillis()
                )
                val (futureWeatherEntities, hourlyWeatherEntities) = convertToEntities(futureWeatherResult.data)
                with(futureWeatherDao) {
                    clearFutureWeather()
                    insertFutureWeather(futureWeatherEntities)
                    insertHourlyWeather(hourlyWeatherEntities)
                    ResultWrapper.Success(getFutureWeatherWithHourly())
                }
            }
            is ResultWrapper.Error -> ResultWrapper.Error(futureWeatherResult.error)
        }

    private fun isFetchNewDataNeeded(
        isRefresh: Boolean,
        currentLocation: Location,
        lastLocation: Location?,
        currentTimeMillis: Long,
        lastRequestTimeMillis: Long
    ): Boolean =
        isRefresh || currentLocation != lastLocation || currentTimeMillis - lastRequestTimeMillis > FORECAST_EXPIRE_TIME_MILLIS

    private fun convertToCurrentWeatherEntity(weatherResponse: CurrentWeatherResponse): CurrentWeatherEntity =
        CurrentWeatherEntity(
            temperatureMetric = weatherResponse.current.temperatureMetric,
            temperatureImperial = weatherResponse.current.temperatureImperial,
            feelsLikeMetric = weatherResponse.current.feelsLikeMetric,
            feelsLikeImperial = weatherResponse.current.feelsLikeImperial,
            windSpeedMetric = weatherResponse.current.windSpeedMetric,
            windSpeedImperial = weatherResponse.current.windSpeedImperial,
            windDirection = weatherResponse.current.windDirection,
            pressureMetric = weatherResponse.current.pressureMetric,
            pressureImperial = weatherResponse.current.pressureImperial,
            humidity = weatherResponse.current.humidity,
            uvIndex = weatherResponse.current.uvIndex,
            description = weatherResponse.current.condition.description,
            iconUrl = weatherResponse.current.condition.iconUrl
        )

    private fun convertToEntities(futureWeatherResponse: FutureWeatherResponse): Pair<List<FutureWeatherEntity>, List<HourlyWeatherEntity>> {
        val futureWeatherEntities = mutableListOf<FutureWeatherEntity>()
        val hourlyWeatherEntities = mutableListOf<HourlyWeatherEntity>()
        futureWeatherResponse.forecast.forecastDays.forEach { forecastDay ->
            val dayOfWeekInt = convertToDayOfWeek(forecastDay.timestampSec)
            futureWeatherEntities.add(
                FutureWeatherEntity(
                    dayOfWeekInt = dayOfWeekInt,
                    timestampSec = forecastDay.timestampSec,
                    avgTemperatureMetric = forecastDay.day.avgTemperatureMetric,
                    avgTemperatureImperial = forecastDay.day.avgTemperatureImperial,
                    avgHumidity = forecastDay.day.avgHumidity,
                    description = forecastDay.day.condition.description,
                    iconUrl = forecastDay.day.condition.iconUrl,
                    uvIndex = forecastDay.day.uvIndex
                )
            )
            forecastDay.hours.forEach { hour ->
                hourlyWeatherEntities.add(
                    HourlyWeatherEntity(
                        dayOfWeekInt = dayOfWeekInt,
                        hourOfDayInt = convertToHourOfDay(hour.timestampSec),
                        timestampSec = hour.timestampSec,
                        temperatureMetric = hour.temperatureMetric,
                        temperatureImperial = hour.temperatureImperial,
                        windSpeedMetric = hour.windSpeedMetric,
                        windSpeedImperial = hour.windSpeedImperial,
                        windDegrees = hour.windDegrees,
                        pressureMetric = hour.pressureMetric,
                        pressureImperial = hour.pressureImperial,
                    )
                )
            }
        }
        return Pair(futureWeatherEntities, hourlyWeatherEntities)
    }

    private fun updateLastWeatherRequestParams(weatherType: WeatherType, location: Location, requestTimeMillis: Long) {
        with(lastWeatherRequestParamsProvider) {
            setLocation(weatherType, location)
            setRequestTime(weatherType, requestTimeMillis)
        }
    }

    private fun convertToDayOfWeek(timestampSec: Long): Int {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = TimeUnit.SECONDS.toMillis(timestampSec)
        }
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    private fun convertToHourOfDay(timestampSec: Long): Int {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = TimeUnit.SECONDS.toMillis(timestampSec)
        }
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    private companion object {
        const val FORECAST_EXPIRE_TIME_MILLIS = 15 * 60 * 1000
    }
}