package com.example.forecastme.domain.settings

import com.example.forecastme.domain.model.Location
import com.example.forecastme.domain.model.UnitSystem
import kotlinx.coroutines.flow.Flow

interface SettingsInteractor {
    fun observeUnitSystem(): Flow<UnitSystem>
    fun observeLocation(): Flow<Location>
    suspend fun setSelectedUnitSystem(unitSystem: UnitSystem)
}