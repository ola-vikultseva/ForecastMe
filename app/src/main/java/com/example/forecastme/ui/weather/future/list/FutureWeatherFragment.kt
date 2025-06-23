package com.example.forecastme.ui.weather.future.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forecastme.R
import com.example.forecastme.databinding.FragmentFutureWeatherBinding
import com.example.forecastme.ui.weather.model.WeatherUiState
import com.example.forecastme.ui.weather.future.list.model.FutureWeatherUiModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FutureWeatherFragment : Fragment(R.layout.fragment_future_weather) {

    private var _binding: FragmentFutureWeatherBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FutureWeatherViewModel by viewModels()
    private lateinit var activity: AppCompatActivity
    private lateinit var actionBar: ActionBar
    private lateinit var futureWeatherAdapter: FutureWeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFutureWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        activity = requireActivity() as AppCompatActivity
        actionBar = activity.supportActionBar!!
        updateDateToNextWeek()
        futureWeatherAdapter = FutureWeatherAdapter(
            context = activity,
            cityName = "",
            futureWeatherItems = emptyList()
        ) { futureWeatherItem, cityName ->
            FutureWeatherFragmentDirections.actionFutureWeatherFragmentToDetailedWeatherFragment(
                selectedDayOfWeek = futureWeatherItem.dayOfWeekInt,
                cityName = cityName
            ).let(findNavController()::navigate)
        }
        binding.recyclerViewFutureWeather.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = futureWeatherAdapter
        }
        viewModel.futureWeather.observe(viewLifecycleOwner) {
            when (it) {
                is WeatherUiState.Loading -> {
                    hideData()
                    hideError()
                    showLoading()
                }
                is WeatherUiState.Data -> {
                    hideLoading()
                    hideError()
                    showData(it.data)
                }
                is WeatherUiState.Error -> {
                    hideLoading()
                    hideData()
                    showError(it.errorMessage)
                }
            }
        }
        binding.swipeRefreshLayoutFutureWeather.setOnRefreshListener {
            viewModel.refreshFutureWeather()
            binding.swipeRefreshLayoutFutureWeather.isRefreshing = false
        }
    }

    private fun updateDateToNextWeek() {
        actionBar.title = getString(R.string.future_weather_screen_action_bar_title)
    }

    private fun showLoading() {
        binding.layoutLoading.root.isVisible = true
    }

    private fun hideLoading() {
        binding.layoutLoading.root.isVisible = false
    }

    private fun showData(futureWeatherUiModel: FutureWeatherUiModel) {
        actionBar.subtitle = futureWeatherUiModel.cityName
        futureWeatherAdapter.apply {
            cityName = futureWeatherUiModel.cityName
            futureWeatherItems = futureWeatherUiModel.data
            notifyDataSetChanged()
        }
        binding.recyclerViewFutureWeather.isVisible = true
    }

    private fun hideData() {
        binding.recyclerViewFutureWeather.isVisible = false
    }

    private fun showError(errorMessage: String) {
        binding.layoutError.textViewErrorMessage.text = errorMessage
        binding.layoutError.root.isVisible = true
    }

    private fun hideError() {
        binding.layoutError.root.isVisible = false
    }
}
