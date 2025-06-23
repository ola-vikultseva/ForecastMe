package com.example.forecastme.data.db.entity.future

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "future_weather")
data class FutureWeatherEntity(
    @PrimaryKey
    @ColumnInfo(name = "day_of_week_int")
    val dayOfWeekInt: Int,
    @ColumnInfo(name = "timestamp_sec")
    val timestampSec: Long,
    @ColumnInfo(name = "avg_temperature_metric")
    val avgTemperatureMetric: Double,
    @ColumnInfo(name = "avg_temperature_imperial")
    val avgTemperatureImperial: Double,
    @ColumnInfo(name = "avg_humidity")
    val avgHumidity: Int,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "icon_url")
    val iconUrl: String,
    @ColumnInfo(name = "uv_index")
    val uvIndex: Double
)