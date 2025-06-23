package com.example.forecastme.ui.weather.current

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.forecastme.R
import com.example.forecastme.domain.model.Location
import com.example.forecastme.domain.weather.current.CurrentWeatherInteractor
import com.example.forecastme.domain.weather.current.model.CurrentWeather
import com.example.forecastme.domain.weather.model.WeatherState
import com.example.forecastme.ui.utils.ErrorFormatter
import com.example.forecastme.ui.utils.StringProvider
import com.example.forecastme.ui.utils.UnitSystemFormatter
import com.example.forecastme.ui.weather.model.WeatherUiState
import com.example.forecastme.ui.weather.current.model.CurrentWeatherUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val currentWeatherInteractor: CurrentWeatherInteractor,
    private val stringProvider: StringProvider,
    private val unitSystemFormatter: UnitSystemFormatter,
    private val errorFormatter: ErrorFormatter
) : ViewModel() {

    val currentWeather: LiveData<WeatherUiState<CurrentWeatherUiModel>> =
        currentWeatherInteractor.observeCurrentWeather().map { currentWeatherState ->
            when (currentWeatherState) {
                is WeatherState.Loading -> WeatherUiState.Loading()
                is WeatherState.Data -> WeatherUiState.Data(
                    convertToCurrentWeatherUiModel(currentWeatherState.data, currentWeatherState.location)
                )
                is WeatherState.Error -> WeatherUiState.Error(errorFormatter.getErrorMessage(currentWeatherState.error))
            }
        }.asLiveData(viewModelScope.coroutineContext)

    fun refreshCurrentWeather() {
        viewModelScope.launch {
            currentWeatherInteractor.refreshCurrentWeather()
        }
    }

    private fun convertToCurrentWeatherUiModel(
        currentWeather: CurrentWeather,
        location: Location
    ): CurrentWeatherUiModel = CurrentWeatherUiModel(
        cityName = location.cityName,
        description = currentWeather.description,
        temperature = unitSystemFormatter.formatTemperature(currentWeather.temperature),
        feelsLike = stringProvider.getString(
            R.string.label_feels_like,
            unitSystemFormatter.formatTemperature(currentWeather.feelsLike)
        ),
        wind = stringProvider.getString(
            R.string.label_wind,
            currentWeather.windDirection,
            unitSystemFormatter.formatWindSpeed(currentWeather.windSpeed)
        ),
        pressure = stringProvider.getString(
            R.string.label_pressure,
            unitSystemFormatter.formatPressure(currentWeather.pressure)
        ),
        humidity = stringProvider.getString(R.string.label_humidity, currentWeather.humidity.toString()),
        uvIndex = stringProvider.getString(
            R.string.label_uv_index,
            String.format(Locale.getDefault(), "%.0f", currentWeather.uvIndex)
        ),
        iconUrl = currentWeather.iconUrl
    )
}
