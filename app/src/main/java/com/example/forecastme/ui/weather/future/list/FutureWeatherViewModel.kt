package com.example.forecastme.ui.weather.future.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.forecastme.domain.model.Location
import com.example.forecastme.domain.weather.future.list.FutureWeatherInteractor
import com.example.forecastme.domain.weather.future.list.model.FutureWeather
import com.example.forecastme.domain.weather.model.WeatherState
import com.example.forecastme.ui.utils.ErrorFormatter
import com.example.forecastme.ui.utils.UnitSystemFormatter
import com.example.forecastme.utils.formatMillis
import com.example.forecastme.ui.weather.model.WeatherUiState
import com.example.forecastme.ui.weather.future.list.model.FutureWeatherItem
import com.example.forecastme.ui.weather.future.list.model.FutureWeatherUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class FutureWeatherViewModel @Inject constructor(
    private val futureWeatherInteractor: FutureWeatherInteractor,
    private val unitSystemFormatter: UnitSystemFormatter,
    private val errorFormatter: ErrorFormatter,
    private val simpleDateFormat: SimpleDateFormat
) : ViewModel() {

    val futureWeather: LiveData<WeatherUiState<FutureWeatherUiModel>> =
        futureWeatherInteractor.observeFutureWeather().map { futureWeatherState ->
            when (futureWeatherState) {
                is WeatherState.Loading -> WeatherUiState.Loading()
                is WeatherState.Data -> WeatherUiState.Data(
                    convertToFutureWeatherUiModel(futureWeatherState.data, futureWeatherState.location)
                )
                is WeatherState.Error -> WeatherUiState.Error(errorFormatter.getErrorMessage(futureWeatherState.error))
            }
        }.asLiveData(viewModelScope.coroutineContext)

    fun refreshFutureWeather() {
        viewModelScope.launch {
            futureWeatherInteractor.refreshFutureWeather()
        }
    }

    private fun convertToFutureWeatherUiModel(
        futureWeatherList: List<FutureWeather>,
        location: Location
    ): FutureWeatherUiModel {
        val sortedFutureWeatherList = futureWeatherList.sortedBy { it.timestampSec }
        val futureWeatherItems = sortedFutureWeatherList.map { futureWeather ->
            FutureWeatherItem(
                date = simpleDateFormat.formatMillis(TimeUnit.SECONDS.toMillis(futureWeather.timestampSec)),
                dayOfWeekInt = futureWeather.dayOfWeekInt,
                dayOfWeekName = DateFormatSymbols.getInstance().weekdays[futureWeather.dayOfWeekInt],
                temperature = unitSystemFormatter.formatTemperature(futureWeather.temperature),
                description = futureWeather.description,
                iconUrl = futureWeather.iconUrl
            )
        }
        return FutureWeatherUiModel(cityName = location.cityName, data = futureWeatherItems)
    }
}
