package com.example.forecastme.ui.utils

interface UnitSystemFormatter {
    fun formatTemperature(temperature: Double): String
    fun formatWindSpeed(windSpeed: Double): String
    fun formatPressure(pressure: Double): String
}