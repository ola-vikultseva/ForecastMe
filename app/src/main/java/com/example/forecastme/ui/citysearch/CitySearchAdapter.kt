package com.example.forecastme.ui.citysearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.forecastme.databinding.ItemCityBinding

import com.example.forecastme.ui.citysearch.model.CityItem

class CitySearchAdapter(
    var cityList: List<CityItem>,
    private val itemClickListener: (CityItem) -> Unit
) : RecyclerView.Adapter<CitySearchAdapter.CityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val binding = ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityViewHolder(binding)
    }

    override fun getItemCount(): Int = cityList.size

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) =
        holder.bind(cityList[position])

    inner class CityViewHolder(binding: ItemCityBinding) : RecyclerView.ViewHolder(binding.root) {
        private val cityName = binding.textViewCityName

        init {
            binding.root.setOnClickListener {
                itemClickListener(cityList[adapterPosition])
            }
        }

        fun bind(cityItem: CityItem) {
            cityName.text = cityItem.name
        }
    }
}