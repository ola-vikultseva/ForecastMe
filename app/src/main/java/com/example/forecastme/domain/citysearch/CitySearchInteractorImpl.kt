package com.example.forecastme.domain.citysearch

import com.example.forecastme.data.model.ResultWrapper
import com.example.forecastme.data.datasources.CityDataSource
import com.example.forecastme.data.network.response.CityResponse
import com.example.forecastme.data.providers.LocationProvider
import com.example.forecastme.domain.citysearch.model.CitySearchState
import com.example.forecastme.domain.model.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class CitySearchInteractorImpl @Inject constructor(
    private val cityDataSource: CityDataSource,
    private val locationProvider: LocationProvider
) : CitySearchInteractor {

    private val _citySearchStateFlow = MutableStateFlow<CitySearchState>(CitySearchState.Empty)

    override fun observeCitySearch(): Flow<CitySearchState> = _citySearchStateFlow.asStateFlow()

    override suspend fun searchCity(query: String) {
        if (query.length <= MINIMUM_QUERY_LENGTH) {
            _citySearchStateFlow.value = CitySearchState.Empty
        } else {
            _citySearchStateFlow.value = CitySearchState.Loading
            when (val result = cityDataSource.searchCity(query)) {
                is ResultWrapper.Loading -> return
                is ResultWrapper.Success -> {
                    _citySearchStateFlow.value = CitySearchState.SearchResult(convertToLocationList(result.data))
                }

                is ResultWrapper.Error -> {
                    _citySearchStateFlow.value = CitySearchState.Error(result.error)
                }
            }
        }
    }

    override suspend fun setSelectedLocation(location: Location) =
        locationProvider.updateLocation(location)

    private fun convertToLocationList(cityResponse: CityResponse): List<Location> =
        cityResponse.results.map { result ->
            Location(
                cityName = result.formatted,
                latitude = result.geometry.lat,
                longitude = result.geometry.lng
            )
        }

    private companion object {
        const val MINIMUM_QUERY_LENGTH = 2
    }
}