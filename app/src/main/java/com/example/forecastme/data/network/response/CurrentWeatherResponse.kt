package com.example.forecastme.data.network.response

import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @SerializedName("current")
    val current: Current
)

data class Current(
    @SerializedName("temp_c")
    val temperatureMetric: Double,
    @SerializedName("temp_f")
    val temperatureImperial: Double,
    @SerializedName("feelslike_c")
    val feelsLikeMetric: Double,
    @SerializedName("feelslike_f")
    val feelsLikeImperial: Double,
    @SerializedName("wind_kph")
    val windSpeedMetric: Double,
    @SerializedName("wind_mph")
    val windSpeedImperial: Double,
    @SerializedName("wind_dir")
    val windDirection: String,
    @SerializedName("pressure_mb")
    val pressureMetric: Double,
    @SerializedName("pressure_in")
    val pressureImperial: Double,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("uv")
    val uvIndex: Double,
    @SerializedName("condition")
    val condition: Condition
)