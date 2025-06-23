package com.example.forecastme.ui.citysearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.forecastme.domain.model.Location
import com.example.forecastme.domain.citysearch.CitySearchInteractor
import com.example.forecastme.domain.citysearch.model.CitySearchState
import com.example.forecastme.ui.utils.ErrorFormatter
import com.example.forecastme.ui.citysearch.model.CityItem
import com.example.forecastme.ui.citysearch.model.CitySearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitySearchViewModel @Inject constructor(
    private val citySearchInteractor: CitySearchInteractor,
    private val errorFormatter: ErrorFormatter
) : ViewModel() {

    val citySearch: LiveData<CitySearchUiState> = citySearchInteractor.observeCitySearch().map { citySearchState ->
        when (citySearchState) {
            CitySearchState.Loading -> CitySearchUiState.Loading
            CitySearchState.Empty -> CitySearchUiState.Empty
            is CitySearchState.SearchResult -> convertToCitySearchUIState(citySearchState.cityList)
            is CitySearchState.Error -> CitySearchUiState.Error(errorFormatter.getErrorMessage(citySearchState.error))
        }
    }.asLiveData(viewModelScope.coroutineContext)

    fun onQueryTextChanged(query: String) {
        viewModelScope.launch {
            citySearchInteractor.searchCity(query)
        }
    }

    fun onCityItemClicked(cityItem: CityItem) {
        viewModelScope.launch {
            citySearchInteractor.setSelectedLocation(
                Location(
                    latitude = cityItem.latitude,
                    longitude = cityItem.longitude,
                    cityName = cityItem.name
                )
            )
        }
    }

    private fun convertToCitySearchUIState(locationList: List<Location>): CitySearchUiState =
        if (locationList.isEmpty()) {
            CitySearchUiState.NoMatches
        } else {
            val cityList = locationList.map { location ->
                CityItem(
                    name = location.cityName,
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            }
            CitySearchUiState.Data(cityList)
        }
}
