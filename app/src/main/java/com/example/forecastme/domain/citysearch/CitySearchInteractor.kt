package com.example.forecastme.domain.citysearch

import com.example.forecastme.domain.model.Location
import com.example.forecastme.domain.citysearch.model.CitySearchState
import kotlinx.coroutines.flow.Flow

interface CitySearchInteractor {
    fun observeCitySearch(): Flow<CitySearchState>
    suspend fun searchCity(query: String)
    suspend fun setSelectedLocation(location: Location)
}