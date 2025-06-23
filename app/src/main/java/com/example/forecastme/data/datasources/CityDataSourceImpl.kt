package com.example.forecastme.data.datasources

import com.example.forecastme.data.utils.ErrorType
import com.example.forecastme.data.model.ResultWrapper
import com.example.forecastme.data.network.GeocodingApiService
import com.example.forecastme.data.network.response.CityResponse
import com.example.forecastme.data.utils.NoConnectivityException

class CityDataSourceImpl(
    private val geocodingApiService: GeocodingApiService
) : CityDataSource {

    override suspend fun searchCity(query: String): ResultWrapper<CityResponse> =
        try {
            ResultWrapper.Success(geocodingApiService.getQueryLocationList(query))
        } catch (exception: Exception) {
            ResultWrapper.Error(convertToErrorType(exception))
        }

    private fun convertToErrorType(exception: Exception): ErrorType =
        when (exception) {
            is NoConnectivityException -> ErrorType.NO_INTERNET_ACCESS
            else -> ErrorType.UNKNOWN
        }
}
