package com.example.forecastme.ui.weather.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.forecastme.R
import com.example.forecastme.databinding.FragmentCurrentWeatherBinding
import com.example.forecastme.utils.withProtocolIfMissing
import com.example.forecastme.ui.weather.model.WeatherUiState
import com.example.forecastme.ui.weather.current.model.CurrentWeatherUiModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrentWeatherFragment : Fragment() {

    private var _binding: FragmentCurrentWeatherBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CurrentWeatherViewModel by viewModels()
    private lateinit var activity: AppCompatActivity
    private lateinit var actionBar: ActionBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        activity = requireActivity() as AppCompatActivity
        actionBar = activity.supportActionBar!!
        updateDateToToday()
        viewModel.currentWeather.observe(viewLifecycleOwner) { currentWeatherUIState ->
            when (currentWeatherUIState) {
                is WeatherUiState.Loading -> {
                    hideData()
                    hideError()
                    showLoading()
                }
                is WeatherUiState.Data -> {
                    hideLoading()
                    hideError()
                    showData(currentWeatherUIState.data)
                }
                is WeatherUiState.Error -> {
                    hideLoading()
                    hideData()
                    showError(currentWeatherUIState.errorMessage)
                }
            }
        }
        binding.swipeRefreshLayoutCurrentWeather.setOnRefreshListener {
            viewModel.refreshCurrentWeather()
            binding.swipeRefreshLayoutCurrentWeather.isRefreshing = false
        }
    }

    private fun updateDateToToday() {
        actionBar.title = getString(R.string.current_weather_screen_action_bar_title)
    }

    private fun showLoading() {
        binding.layoutLoading.root.isVisible = true
    }

    private fun hideLoading() {
        binding.layoutLoading.root.isVisible = false
    }

    private fun showData(currentWeatherUiModel: CurrentWeatherUiModel) {
        actionBar.subtitle = currentWeatherUiModel.cityName
        updateDescription(currentWeatherUiModel.description)
        updateTemperatures(currentWeatherUiModel.temperature, currentWeatherUiModel.feelsLike)
        updateWind(currentWeatherUiModel.wind)
        updatePressure(currentWeatherUiModel.pressure)
        updateHumidity(currentWeatherUiModel.humidity)
        updateUvIndex(currentWeatherUiModel.uvIndex)
        Glide
            .with(this)
            .load(currentWeatherUiModel.iconUrl.withProtocolIfMissing())
            .into(binding.imageViewIcon)
        binding.groupData.isVisible = true
    }

    private fun hideData() {
        binding.groupData.isVisible = false
    }

    private fun showError(errorMessage: String) {
        binding.layoutError.textViewErrorMessage.text = errorMessage
        binding.layoutError.root.isVisible = true
    }

    private fun hideError() {
        binding.layoutError.root.isVisible = false
    }

    private fun updateDescription(weatherDescription: String) {
        binding.textViewDescription.text = weatherDescription
    }

    private fun updateTemperatures(temperature: String, feelsLikeTemperature: String) {
        binding.textViewTemperature.text = temperature
        binding.textViewTemperatureFeelsLike.text = feelsLikeTemperature
    }

    private fun updateWind(wind: String) {
        binding.layoutWeatherData.textViewWind.text = wind
    }

    private fun updatePressure(pressure: String) {
        binding.layoutWeatherData.textViewPressure.text = pressure
    }

    private fun updateHumidity(humidity: String) {
        binding.layoutWeatherData.textViewHumidity.text = humidity
    }

    private fun updateUvIndex(uvIndex: String) {
        binding.layoutWeatherData.textViewUvIndex.text = uvIndex
    }
}