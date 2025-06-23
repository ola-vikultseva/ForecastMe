package com.example.forecastme.ui.weather.future.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forecastme.R
import com.example.forecastme.domain.weather.future.detail.DetailedWeatherInteractor
import com.example.forecastme.domain.weather.future.detail.model.DetailedWeather
import com.example.forecastme.ui.utils.StringProvider
import com.example.forecastme.ui.utils.UnitSystemFormatter
import com.example.forecastme.utils.formatMillis
import com.example.forecastme.ui.weather.future.detail.model.DailyTemperature
import com.example.forecastme.ui.weather.future.detail.model.DetailedWeatherUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class DetailedWeatherViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val detailedWeatherInteractor: DetailedWeatherInteractor,
    private val stringProvider: StringProvider,
    private val unitSystemFormatter: UnitSystemFormatter,
    private val simpleDateFormat: SimpleDateFormat
) : ViewModel() {

    private val dayOfWeek: Int = requireNotNull(savedStateHandle[SELECTED_DAY_OF_WEEK_KEY])
    private val cityName: String = requireNotNull(savedStateHandle[CITY_NAME_KEY])

    private val _detailedWeather = MutableLiveData<DetailedWeatherUiModel>()
    val detailedWeather: LiveData<DetailedWeatherUiModel>
        get() = _detailedWeather

    init {
        viewModelScope.launch {
            _detailedWeather.value =
                convertToDetailedWeatherUiModel(detailedWeatherInteractor.getDetailedWeather(dayOfWeek))
        }
    }

    private fun convertToDetailedWeatherUiModel(detailedWeather: DetailedWeather): DetailedWeatherUiModel =
        DetailedWeatherUiModel(
            cityName = cityName,
            date = simpleDateFormat.formatMillis(TimeUnit.SECONDS.toMillis(detailedWeather.timestampSec)),
            description = detailedWeather.description,
            temperature = unitSystemFormatter.formatTemperature(detailedWeather.temperature),
            dailyTemperature = DailyTemperature(
                morning = unitSystemFormatter.formatTemperature(detailedWeather.dailyTemperature.morning),
                day = unitSystemFormatter.formatTemperature(detailedWeather.dailyTemperature.day),
                evening = unitSystemFormatter.formatTemperature(detailedWeather.dailyTemperature.evening),
                night = unitSystemFormatter.formatTemperature(detailedWeather.dailyTemperature.night)
            ),
            wind = stringProvider.getString(
                R.string.label_wind,
                detailedWeather.windDirection,
                unitSystemFormatter.formatWindSpeed(detailedWeather.windSpeed)
            ),
            pressure = stringProvider.getString(
                R.string.label_pressure,
                unitSystemFormatter.formatPressure(detailedWeather.pressure)
            ),
            humidity = stringProvider.getString(R.string.label_humidity, detailedWeather.humidity.toString()),
            uvIndex = stringProvider.getString(
                R.string.label_uv_index,
                String.format(Locale.getDefault(), "%.0f", detailedWeather.uvIndex)
            ),
            iconUrl = detailedWeather.iconUrl
        )

    private companion object {
        const val SELECTED_DAY_OF_WEEK_KEY = "selectedDayOfWeek"
        const val CITY_NAME_KEY = "cityName"
    }
}
