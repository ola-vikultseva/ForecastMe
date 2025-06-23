package com.example.forecastme.data.providers

import com.example.forecastme.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationProvider {
    fun observeLocation(): Flow<Location>
    suspend fun updateLocation(location: Location)
}