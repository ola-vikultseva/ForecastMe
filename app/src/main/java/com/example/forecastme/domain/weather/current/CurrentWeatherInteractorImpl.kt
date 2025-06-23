package com.example.forecastme.domain.weather.current

import com.example.forecastme.domain.model.Location
import com.example.forecastme.data.model.ResultWrapper
import com.example.forecastme.data.db.entity.current.CurrentWeatherEntity
import com.example.forecastme.data.providers.LocationProvider
import com.example.forecastme.data.providers.UnitSystemProvider
import com.example.forecastme.domain.WeatherRepository
import com.example.forecastme.domain.weather.current.model.CurrentWeather
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

class CurrentWeatherInteractorImpl @Inject constructor(
    private val locationProvider: LocationProvider,
    private val unitSystemProvider: UnitSystemProvider,
    private val weatherRepository: WeatherRepository
) : CurrentWeatherInteractor {

    private val _refreshFlow = MutableSharedFlow<Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeCurrentWeather(): Flow<WeatherState<CurrentWeather>> {
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
                emit(getCurrentWeather(location, isRefresh))
            }
        }
    }

    override suspend fun refreshCurrentWeather() {
        _refreshFlow.emit(Unit)
    }

    private suspend fun getCurrentWeather(
        location: Location,
        isRefresh: Boolean
    ): WeatherState<CurrentWeather> =
        when (val currentWeatherResult = weatherRepository.getCurrentWeather(location, isRefresh)) {
            is ResultWrapper.Loading -> WeatherState.Loading()
            is ResultWrapper.Success -> WeatherState.Data(convertToCurrentWeather(currentWeatherResult.data), location)
            is ResultWrapper.Error -> WeatherState.Error(currentWeatherResult.error)
        }

    private fun convertToCurrentWeather(currentWeatherEntity: CurrentWeatherEntity): CurrentWeather {
        val isMetric = unitSystemProvider.isMetric
        return CurrentWeather(
            description = currentWeatherEntity.description,
            temperature = if (isMetric) currentWeatherEntity.temperatureMetric else currentWeatherEntity.temperatureImperial,
            feelsLike = if (isMetric) currentWeatherEntity.feelsLikeMetric else currentWeatherEntity.feelsLikeImperial,
            windSpeed = if (isMetric) currentWeatherEntity.windSpeedMetric else currentWeatherEntity.windSpeedImperial,
            windDirection = currentWeatherEntity.windDirection,
            pressure = if (isMetric) currentWeatherEntity.pressureMetric else currentWeatherEntity.pressureImperial,
            humidity = currentWeatherEntity.humidity,
            uvIndex = currentWeatherEntity.uvIndex,
            iconUrl = currentWeatherEntity.iconUrl
        )
    }
}