package com.example.forecastme.domain.weather.future.list

import com.example.forecastme.domain.model.Location
import com.example.forecastme.data.model.ResultWrapper
import com.example.forecastme.data.db.entity.future.FutureWeatherWithHourly
import com.example.forecastme.data.providers.LocationProvider
import com.example.forecastme.data.providers.UnitSystemProvider
import com.example.forecastme.domain.WeatherRepository
import com.example.forecastme.domain.weather.future.list.model.FutureWeather
import com.example.forecastme.domain.weather.model.WeatherState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

class FutureWeatherInteractorImpl @Inject constructor(
    private val locationProvider: LocationProvider,
    private val unitSystemProvider: UnitSystemProvider,
    private val weatherRepository: WeatherRepository
) : FutureWeatherInteractor {

    private val _refreshFlow = MutableSharedFlow<Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeFutureWeather(): Flow<WeatherState<List<FutureWeather>>> {
        val paramsFlow = combine(
            locationProvider.observeLocation(),
            unitSystemProvider.observeUnitSystem()
        ) { location, _ ->
            location to false
        }
        val refreshFlow = _refreshFlow.mapLatest { _ ->
            val location = locationProvider.observeLocation().first()
            location to true
        }
        return merge(paramsFlow, refreshFlow).flatMapLatest { (location, isRefresh) ->
            flow {
                emit(WeatherState.Loading())
                emit(getFutureWeather(FORECAST_DAYS_COUNT, location, isRefresh))
            }
        }
    }

    override suspend fun refreshFutureWeather() {
        _refreshFlow.emit(Unit)
    }

    private suspend fun getFutureWeather(
        daysCount: Int,
        location: Location,
        isRefresh: Boolean
    ): WeatherState<List<FutureWeather>> {
        return when (val result = weatherRepository.getFutureWeather(daysCount, location, isRefresh)) {
            is ResultWrapper.Loading -> WeatherState.Loading()
            is ResultWrapper.Success -> WeatherState.Data(convertToFutureWeather(result.data), location)
            is ResultWrapper.Error -> WeatherState.Error(result.error)
        }
    }

    private fun convertToFutureWeather(futureWeatherWithHourlyList: List<FutureWeatherWithHourly>): List<FutureWeather> {
        val isMetric = unitSystemProvider.isMetric
        return futureWeatherWithHourlyList.map { futureWeatherWithHourly ->
            FutureWeather(
                dayOfWeekInt = futureWeatherWithHourly.futureWeather.dayOfWeekInt,
                timestampSec = futureWeatherWithHourly.futureWeather.timestampSec,
                description = futureWeatherWithHourly.futureWeather.description,
                temperature = if (isMetric) futureWeatherWithHourly.futureWeather.avgTemperatureMetric else futureWeatherWithHourly.futureWeather.avgTemperatureImperial,
                iconUrl = futureWeatherWithHourly.futureWeather.iconUrl
            )
        }
    }

    private companion object {
        const val FORECAST_DAYS_COUNT = 7
    }
}