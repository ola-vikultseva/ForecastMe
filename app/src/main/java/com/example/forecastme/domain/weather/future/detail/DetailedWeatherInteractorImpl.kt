package com.example.forecastme.domain.weather.future.detail

import com.example.forecastme.data.db.FutureWeatherDao
import com.example.forecastme.data.db.entity.future.FutureWeatherWithHourly
import com.example.forecastme.data.db.entity.future.HourlyWeatherEntity
import com.example.forecastme.data.providers.UnitSystemProvider
import com.example.forecastme.domain.weather.future.detail.model.DailyTemperature
import com.example.forecastme.domain.weather.future.detail.model.DetailedWeather
import javax.inject.Inject
import kotlin.math.roundToInt

class DetailedWeatherInteractorImpl @Inject constructor(
    private val unitSystemProvider: UnitSystemProvider,
    private val futureWeatherDao: FutureWeatherDao
) : DetailedWeatherInteractor {

    private val isMetric = unitSystemProvider.isMetric

    override suspend fun getDetailedWeather(dayOfWeek: Int): DetailedWeather =
        convertToDetailedWeather(futureWeatherDao.getFutureWeatherWithHourlyForDay(dayOfWeek))

    private fun convertToDetailedWeather(futureWeatherWithHourly: FutureWeatherWithHourly): DetailedWeather =
        DetailedWeather(
            timestampSec = futureWeatherWithHourly.futureWeather.timestampSec,
            description = futureWeatherWithHourly.futureWeather.description,
            temperature = if (isMetric) futureWeatherWithHourly.futureWeather.avgTemperatureMetric else futureWeatherWithHourly.futureWeather.avgTemperatureImperial,
            dailyTemperature = calculateDailyTemperature(futureWeatherWithHourly.hourlyWeatherList),
            windSpeed = calculateAverageWindSpeed(futureWeatherWithHourly.hourlyWeatherList),
            windDirection = convertToCompassDirection(calculateAverageWindDegrees(futureWeatherWithHourly.hourlyWeatherList)),
            pressure = calculateAveragePressure(futureWeatherWithHourly.hourlyWeatherList),
            humidity = futureWeatherWithHourly.futureWeather.avgHumidity,
            uvIndex = futureWeatherWithHourly.futureWeather.uvIndex,
            iconUrl = futureWeatherWithHourly.futureWeather.iconUrl
        )

    private fun convertToCompassDirection(degrees: Int): String {
        val compassDirections =
            listOf("N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW")
        val directionRange = 360.0 / compassDirections.size
        val index = (degrees / directionRange).roundToInt() % compassDirections.size
        return compassDirections[index]
    }

    private fun calculateAverageWindSpeed(hourlyWeatherEntities: List<HourlyWeatherEntity>): Double =
        hourlyWeatherEntities.map { if (isMetric) it.windSpeedMetric else it.windSpeedImperial }.average()

    private fun calculateAverageWindDegrees(hourlyWeatherEntities: List<HourlyWeatherEntity>): Int =
        hourlyWeatherEntities.map { it.windDegrees }.average().roundToInt()

    private fun calculateAveragePressure(hourlyWeatherEntities: List<HourlyWeatherEntity>): Double =
        hourlyWeatherEntities.map { if (isMetric) it.pressureMetric else it.pressureImperial }.average()

    private fun calculateDailyTemperature(hourlyWeatherEntities: List<HourlyWeatherEntity>): DailyTemperature {
        val nightHours = 0..5
        val morningHours = 6..11
        val dayHours = 12..17
        val eveningHours = 18..23

        fun averageTemperatureForHours(range: IntRange): Double =
            hourlyWeatherEntities.subList(range.first, range.last + 1)
                .map { if (isMetric) it.temperatureMetric else it.temperatureImperial }
                .average()

        return DailyTemperature(
            morning = averageTemperatureForHours(morningHours),
            day = averageTemperatureForHours(dayHours),
            evening = averageTemperatureForHours(eveningHours),
            night = averageTemperatureForHours(nightHours)
        )
    }
}