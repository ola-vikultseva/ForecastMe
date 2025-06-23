package com.example.forecastme.data.db.entity.future

import androidx.room.Embedded
import androidx.room.Relation

data class FutureWeatherWithHourly(
    @Embedded
    val futureWeather: FutureWeatherEntity,
    @Relation(
        parentColumn = "day_of_week_int",
        entityColumn = "day_of_week_int"
    )
    val hourlyWeatherList: List<HourlyWeatherEntity>
)
