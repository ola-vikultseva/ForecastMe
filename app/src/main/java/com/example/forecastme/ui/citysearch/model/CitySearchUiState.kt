package com.example.forecastme.ui.citysearch.model

sealed class CitySearchUiState {
    data object Empty : CitySearchUiState()
    data object Loading : CitySearchUiState()
    data object NoMatches : CitySearchUiState()
    data class Data(val cityList: List<CityItem>) : CitySearchUiState()
    data class Error(val errorMessage: String) : CitySearchUiState()
}