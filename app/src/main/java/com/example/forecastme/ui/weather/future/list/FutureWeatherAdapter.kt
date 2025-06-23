package com.example.forecastme.ui.weather.future.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.forecastme.databinding.ItemFutureWeatherBinding
import com.example.forecastme.utils.withProtocolIfMissing
import com.example.forecastme.ui.weather.future.list.model.FutureWeatherItem

class FutureWeatherAdapter(
    private val context: Context,
    var cityName: String,
    var futureWeatherItems: List<FutureWeatherItem>,
    private val itemClickListener: (FutureWeatherItem, String) -> Unit
) : RecyclerView.Adapter<FutureWeatherAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = ItemFutureWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun getItemCount(): Int = futureWeatherItems.size

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) = holder.bind(futureWeatherItems[position])

    inner class WeatherViewHolder(binding: ItemFutureWeatherBinding) : RecyclerView.ViewHolder(binding.root) {
        private val date = binding.textViewDate
        private val dayOfWeek = binding.textViewDayOfWeekName
        private val temperature = binding.textViewTemperature
        private val weatherDescription = binding.textViewDescription
        private val weatherIcon = binding.imageViewIcon

        init {
            binding.root.setOnClickListener {
                itemClickListener(futureWeatherItems[adapterPosition], cityName)
            }
        }

        fun bind(futureWeatherItem: FutureWeatherItem) {
            date.text = futureWeatherItem.date
            dayOfWeek.text = futureWeatherItem.dayOfWeekName
            temperature.text = futureWeatherItem.temperature
            weatherDescription.text = futureWeatherItem.description
            Glide
                .with(context)
                .load(futureWeatherItem.iconUrl.withProtocolIfMissing())
                .into(weatherIcon)
        }
    }
}

