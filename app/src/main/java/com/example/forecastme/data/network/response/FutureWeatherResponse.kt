package com.example.forecastme.data.network.response

import com.google.gson.annotations.SerializedName

data class FutureWeatherResponse(
    @SerializedName("forecast")
    val forecast: Forecast
)

data class Forecast(
    @SerializedName("forecastday")
    val forecastDays: List<ForecastDay>
)

data class ForecastDay(
    @SerializedName("date_epoch")
    val timestampSec: Long,
    @SerializedName("day")
    val day: Day,
    @SerializedName("hour")
    val hours: List<Hour>
)

data class Day(
    @SerializedName("avgtemp_c")
    val avgTemperatureMetric: Double,
    @SerializedName("avgtemp_f")
    val avgTemperatureImperial: Double,
    @SerializedName("avghumidity")
    val avgHumidity: Int,
    @SerializedName("condition")
    val condition: Condition,
    @SerializedName("uv")
    val uvIndex: Double
)

data class Hour(
    @SerializedName("time_epoch")
    val timestampSec: Long,
    @SerializedName("temp_c")
    val temperatureMetric: Double,
    @SerializedName("temp_f")
    val temperatureImperial: Double,
    @SerializedName("wind_kph")
    val windSpeedMetric: Double,
    @SerializedName("wind_mph")
    val windSpeedImperial: Double,
    @SerializedName("wind_degree")
    val windDegrees: Int,
    @SerializedName("pressure_mb")
    val pressureMetric: Double,
    @SerializedName("pressure_in")
    val pressureImperial: Double
)
