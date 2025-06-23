package com.example.forecastme.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.forecastme.data.db.WeatherDatabase.Companion.DATABASE_VERSION
import com.example.forecastme.data.db.entity.current.CurrentWeatherEntity
import com.example.forecastme.data.db.entity.future.FutureWeatherEntity
import com.example.forecastme.data.db.entity.future.HourlyWeatherEntity

@Database(
    entities = [CurrentWeatherEntity::class, FutureWeatherEntity::class, HourlyWeatherEntity::class],
    version = DATABASE_VERSION
)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun futureWeatherDao(): FutureWeatherDao

    companion object {
        private const val DATABASE_NAME = "weather.db"
        const val DATABASE_VERSION = 1

        fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            WeatherDatabase::class.java, DATABASE_NAME
        ).build()
    }
}