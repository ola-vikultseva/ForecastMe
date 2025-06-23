package com.example.forecastme.data.db.entity.future

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "hourly_weather",
    primaryKeys = ["day_of_week_int", "hour_of_day_int"],
    foreignKeys = [ForeignKey(
        entity = FutureWeatherEntity::class,
        parentColumns = ["day_of_week_int"],
        childColumns = ["day_of_week_int"],
        onDelete = ForeignKey.CASCADE
    )
    ],
    indices = [Index("hour_of_day_int")]
)
data class HourlyWeatherEntity(
    @ColumnInfo(name = "day_of_week_int")
    val dayOfWeekInt: Int,
    @ColumnInfo(name = "hour_of_day_int")
    val hourOfDayInt: Int,
    @ColumnInfo("timestamp_sec")
    val timestampSec: Long,
    @ColumnInfo("temperature_metric")
    val temperatureMetric: Double,
    @ColumnInfo("temperature_imperial")
    val temperatureImperial: Double,
    @ColumnInfo("wind_speed_metric")
    val windSpeedMetric: Double,
    @ColumnInfo("wind_speed_imperial")
    val windSpeedImperial: Double,
    @ColumnInfo("wind_degrees")
    val windDegrees: Int,
    @ColumnInfo("pressure_metric")
    val pressureMetric: Double,
    @ColumnInfo("pressure_imperial")
    val pressureImperial: Double
)
