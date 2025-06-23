package com.example.forecastme.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecastme.data.db.entity.current.CURRENT_WEATHER_ID
import com.example.forecastme.data.db.entity.current.CurrentWeatherEntity

@Dao
interface CurrentWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(currentWeatherEntry: CurrentWeatherEntity)

    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    suspend fun getWeather(): CurrentWeatherEntity
}