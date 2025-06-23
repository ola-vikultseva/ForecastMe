package com.example.forecastme.ui.weather.future.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.forecastme.databinding.FragmentDetailedWeatherBinding
import com.example.forecastme.utils.withProtocolIfMissing
import com.example.forecastme.ui.weather.future.detail.model.DailyTemperature
import com.example.forecastme.ui.weather.future.detail.model.DetailedWeatherUiModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailedWeatherFragment : Fragment() {

    private var _binding: FragmentDetailedWeatherBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailedWeatherViewModel by viewModels()
    private lateinit var actionBar: ActionBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        actionBar = (requireActivity() as AppCompatActivity).supportActionBar!!
        viewModel.detailedWeather.observe(viewLifecycleOwner) { detailedWeather ->
            showData(detailedWeather)
        }
    }

    private fun showData(detailedWeatherUiModel: DetailedWeatherUiModel) {
        actionBar.apply {
            title = detailedWeatherUiModel.date
            subtitle = detailedWeatherUiModel.cityName
        }
        updateDescription(detailedWeatherUiModel.description)
        updateTemperatures(detailedWeatherUiModel.temperature, detailedWeatherUiModel.dailyTemperature)
        updateWind(detailedWeatherUiModel.wind)
        updatePressure(detailedWeatherUiModel.pressure)
        updateHumidity(detailedWeatherUiModel.humidity)
        updateUvIndex(detailedWeatherUiModel.uvIndex)
        Glide
            .with(this)
            .load(detailedWeatherUiModel.iconUrl.withProtocolIfMissing())
            .into(binding.imageViewIcon)
    }

    private fun updateDescription(description: String) {
        binding.textViewDescription.text = description
    }

    private fun updateTemperatures(temperature: String, dailyTemperature: DailyTemperature) {
        with(binding) {
            textViewTemperature.text = temperature
            layoutTemperatureDayParts.textViewTempMornValue.text = dailyTemperature.morning
            layoutTemperatureDayParts.textViewTempDayValue.text = dailyTemperature.day
            layoutTemperatureDayParts.textViewTempEveValue.text = dailyTemperature.evening
            layoutTemperatureDayParts.textViewTempNightValue.text = dailyTemperature.night
        }
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
