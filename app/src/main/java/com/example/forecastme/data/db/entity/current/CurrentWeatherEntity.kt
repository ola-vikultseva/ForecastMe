package com.example.forecastme.data.db.entity.current

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_WEATHER_ID = 1

@Entity(tableName = "current_weather")
data class CurrentWeatherEntity(
    @PrimaryKey
    val id: Int = CURRENT_WEATHER_ID,
    @ColumnInfo(name = "temperature_metric")
    val temperatureMetric: Double,
    @ColumnInfo(name = "temperature_imperial")
    val temperatureImperial: Double,
    @ColumnInfo(name = "feels_like_metric")
    val feelsLikeMetric: Double,
    @ColumnInfo(name = "feels_like_imperial")
    val feelsLikeImperial: Double,
    @ColumnInfo(name = "wind_speed_metric")
    val windSpeedMetric: Double,
    @ColumnInfo(name = "wind_speed_imperial")
    val windSpeedImperial: Double,
    @ColumnInfo(name = "wind_direction")
    val windDirection: String,
    @ColumnInfo(name = "pressure_metric")
    val pressureMetric: Double,
    @ColumnInfo(name = "pressure_imperial")
    val pressureImperial: Double,
    @ColumnInfo(name = "humidity")
    val humidity: Int,
    @ColumnInfo(name = "uv_index")
    val uvIndex: Double,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "icon_url")
    val iconUrl: String
)