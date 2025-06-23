package com.example.forecastme.data.datasources

import com.example.forecastme.data.model.ResultWrapper
import com.example.forecastme.data.network.response.CityResponse

interface CityDataSource {
    suspend fun searchCity(query: String): ResultWrapper<CityResponse>
}