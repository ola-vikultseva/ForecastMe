package com.example.forecastme.domain.citysearch.model

import com.example.forecastme.data.utils.ErrorType
import com.example.forecastme.domain.model.Location

sealed class CitySearchState {
    data object Empty : CitySearchState()
    data object Loading : CitySearchState()
    data class SearchResult(val cityList: List<Location>) : CitySearchState()
    data class Error(val error: ErrorType) : CitySearchState()
}