package com.example.forecastme.data.providers

import android.content.Context
import androidx.core.content.edit
import com.example.forecastme.domain.model.Location
import com.example.forecastme.data.utils.WeatherType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LastWeatherRequestParamsProviderImpl(
    context: Context
) : PreferenceProvider(context), LastWeatherRequestParamsProvider {

    override fun getLocation(weatherType: WeatherType): Location? {
        val key = when (weatherType) {
            WeatherType.CURRENT -> CURRENT_WEATHER_LOCATION
            WeatherType.FUTURE -> FUTURE_WEATHER_LOCATION
        }
        return preferences.getString(key, null)?.let { Json.decodeFromString(it) }
    }

    override fun setLocation(weatherType: WeatherType, location: Location) {
        val key = when (weatherType) {
            WeatherType.CURRENT -> CURRENT_WEATHER_LOCATION
            WeatherType.FUTURE -> FUTURE_WEATHER_LOCATION
        }
        preferences.edit {
            putString(key, Json.encodeToString(location))
        }
    }

    override fun getRequestTimeMillis(weatherType: WeatherType): Long {
        val key = when (weatherType) {
            WeatherType.CURRENT -> CURRENT_WEATHER_REQUEST_TIME
            WeatherType.FUTURE -> FUTURE_WEATHER_REQUEST_TIME
        }
        return preferences.getLong(key, 0L)
    }

    override fun setRequestTime(weatherType: WeatherType, requestTimeMillis: Long) {
        val key = when (weatherType) {
            WeatherType.CURRENT -> CURRENT_WEATHER_REQUEST_TIME
            WeatherType.FUTURE -> FUTURE_WEATHER_REQUEST_TIME
        }
        preferences.edit {
            putLong(key, requestTimeMillis)
        }
    }

    private companion object {
        const val CURRENT_WEATHER_LOCATION = "CURRENT_WEATHER_LOCATION"
        const val FUTURE_WEATHER_LOCATION = "FUTURE_WEATHER_LOCATION"
        const val CURRENT_WEATHER_REQUEST_TIME = "CURRENT_WEATHER_REQUEST_TIME"
        const val FUTURE_WEATHER_REQUEST_TIME = "FUTURE_WEATHER_REQUEST_TIME"
    }
}