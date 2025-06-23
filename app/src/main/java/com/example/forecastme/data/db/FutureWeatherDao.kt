package com.example.forecastme.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.forecastme.data.db.entity.future.FutureWeatherEntity
import com.example.forecastme.data.db.entity.future.FutureWeatherWithHourly
import com.example.forecastme.data.db.entity.future.HourlyWeatherEntity

@Dao
interface FutureWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFutureWeather(futureWeatherEntities: List<FutureWeatherEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyWeather(hourlyWeatherEntities: List<HourlyWeatherEntity>)

    @Transaction
    @Query("select * from future_weather")
    suspend fun getFutureWeatherWithHourly(): List<FutureWeatherWithHourly>

    @Transaction
    @Query("select * from future_weather where day_of_week_int = :dayOfWeek")
    suspend fun getFutureWeatherWithHourlyForDay(dayOfWeek: Int): FutureWeatherWithHourly

    // Also deletes related child data due to ON DELETE CASCADE.
    @Query("DELETE FROM future_weather")
    suspend fun clearFutureWeather()
}