package com.example.forecastme.di

import android.content.Context
import com.example.forecastme.R
import com.example.forecastme.data.utils.ErrorType
import com.example.forecastme.data.WeatherRepositoryImpl
import com.example.forecastme.data.datasources.CityDataSource
import com.example.forecastme.data.datasources.CityDataSourceImpl
import com.example.forecastme.data.datasources.WeatherDataSource
import com.example.forecastme.data.datasources.WeatherDataSourceImpl
import com.example.forecastme.data.db.CurrentWeatherDao
import com.example.forecastme.data.db.FutureWeatherDao
import com.example.forecastme.data.db.WeatherDatabase
import com.example.forecastme.data.network.ConnectivityInterceptor
import com.example.forecastme.data.network.ConnectivityInterceptorImpl
import com.example.forecastme.data.network.GeocodingApiService
import com.example.forecastme.data.network.WeatherApiService
import com.example.forecastme.data.providers.LastWeatherRequestParamsProvider
import com.example.forecastme.data.providers.LastWeatherRequestParamsProviderImpl
import com.example.forecastme.data.providers.LocationProvider
import com.example.forecastme.data.providers.LocationProviderImpl
import com.example.forecastme.data.providers.UnitSystemProvider
import com.example.forecastme.data.providers.UnitSystemProviderImpl
import com.example.forecastme.domain.WeatherRepository
import com.example.forecastme.ui.utils.ErrorFormatter
import com.example.forecastme.ui.utils.StringProvider
import com.example.forecastme.ui.utils.UnitSystemFormatter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDatabase =
        WeatherDatabase.buildDatabase(context)

    @Provides
    @Singleton
    fun provideCurrentWeatherDao(weatherDatabase: WeatherDatabase): CurrentWeatherDao =
        weatherDatabase.currentWeatherDao()

    @Provides
    @Singleton
    fun provideFutureWeatherDao(weatherDatabase: WeatherDatabase): FutureWeatherDao =
        weatherDatabase.futureWeatherDao()

    @Provides
    @Singleton
    fun provideWeatherApiService(connectivityInterceptor: ConnectivityInterceptor): WeatherApiService =
        WeatherApiService(connectivityInterceptor)

    @Provides
    @Singleton
    fun provideWeatherDataSource(weatherApiService: WeatherApiService): WeatherDataSource =
        WeatherDataSourceImpl(weatherApiService)

    @Provides
    @Singleton
    fun provideGeocodingApiService(connectivityInterceptor: ConnectivityInterceptor): GeocodingApiService =
        GeocodingApiService(connectivityInterceptor)

    @Provides
    @Singleton
    fun provideCityDataSource(geocodingApiService: GeocodingApiService): CityDataSource =
        CityDataSourceImpl(geocodingApiService)

    @Provides
    @Singleton
    fun provideWeatherRepository(
        currentWeatherDao: CurrentWeatherDao,
        futureWeatherDao: FutureWeatherDao,
        weatherDataSource: WeatherDataSource,
        lastWeatherRequestParamsProvider: LastWeatherRequestParamsProvider
    ): WeatherRepository = WeatherRepositoryImpl(
        currentWeatherDao,
        futureWeatherDao,
        weatherDataSource,
        lastWeatherRequestParamsProvider
    )

    @Provides
    @Singleton
    fun provideUnitSystemProvider(@ApplicationContext context: Context): UnitSystemProvider =
        UnitSystemProviderImpl(context)

    @Provides
    @Singleton
    fun provideLocationProvider(@ApplicationContext context: Context): LocationProvider =
        LocationProviderImpl(context)

    @Provides
    @Singleton
    fun provideLastWeatherRequestParamsProvider(
        @ApplicationContext context: Context
    ): LastWeatherRequestParamsProvider = LastWeatherRequestParamsProviderImpl(context)

    @Provides
    @Singleton
    fun provideStringProvider(@ApplicationContext context: Context): StringProvider = object : StringProvider {
        override fun getString(resId: Int): String = context.getString(resId)

        override fun getString(resId: Int, vararg formatArgs: Any): String = context.getString(resId, *formatArgs)
    }

    @Provides
    @Singleton
    fun provideErrorFormatter(@ApplicationContext context: Context): ErrorFormatter = object : ErrorFormatter {
        override fun getErrorMessage(error: ErrorType): String = context.getString(
            when (error) {
                ErrorType.NO_INTERNET_ACCESS -> R.string.no_internet_access_error_message
                ErrorType.UNKNOWN -> R.string.default_error_message
            }
        )
    }

    @Provides
    @Singleton
    fun provideUnitSystemFormatter(
        @ApplicationContext context: Context,
        unitSystemProvider: UnitSystemProvider
    ): UnitSystemFormatter = object : UnitSystemFormatter {
        override fun formatTemperature(temperature: Double): String {
            val unitResId =
                if (unitSystemProvider.isMetric) R.string.unit_temperature_metric else R.string.unit_temperature_imperial
            val unit = context.getString(unitResId)
            return String.format(Locale.getDefault(), "%.0f%s", temperature, unit)
        }

        override fun formatWindSpeed(windSpeed: Double): String {
            val unitResId =
                if (unitSystemProvider.isMetric) R.string.unit_wind_speed_metric else R.string.unit_wind_speed_imperial
            val unit = context.getString(unitResId)
            return String.format(Locale.getDefault(), "%.0f %s", windSpeed, unit)
        }

        override fun formatPressure(pressure: Double): String {
            val unitResId =
                if (unitSystemProvider.isMetric) R.string.unit_pressure_metric else R.string.unit_pressure_imperial
            val unit = context.getString(unitResId)
            return String.format(Locale.getDefault(), "%.0f %s", pressure, unit)
        }
    }

    @Provides
    @Singleton
    fun provideSimpleDateFormat(): SimpleDateFormat = SimpleDateFormat("dd MMMM", Locale.getDefault())

    @Provides
    @Singleton
    fun provideConnectivityInterceptor(@ApplicationContext context: Context): ConnectivityInterceptor =
        ConnectivityInterceptorImpl(context)
}