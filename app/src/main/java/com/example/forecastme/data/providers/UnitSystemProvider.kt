package com.example.forecastme.data.providers

import com.example.forecastme.domain.model.UnitSystem
import kotlinx.coroutines.flow.Flow

interface UnitSystemProvider {
    val isMetric: Boolean
    fun observeUnitSystem(): Flow<UnitSystem>
    suspend fun updateUnitSystem(unitSystem: UnitSystem)
}