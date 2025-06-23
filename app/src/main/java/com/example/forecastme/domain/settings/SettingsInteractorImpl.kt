package com.example.forecastme.domain.settings

import com.example.forecastme.domain.model.Location
import com.example.forecastme.domain.model.UnitSystem
import com.example.forecastme.data.providers.LocationProvider
import com.example.forecastme.data.providers.UnitSystemProvider
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsInteractorImpl @Inject constructor(
    private val unitSystemProvider: UnitSystemProvider,
    private val locationProvider: LocationProvider
) : SettingsInteractor {

    override fun observeUnitSystem(): Flow<UnitSystem> = unitSystemProvider.observeUnitSystem()

    override fun observeLocation(): Flow<Location> = locationProvider.observeLocation()

    override suspend fun setSelectedUnitSystem(unitSystem: UnitSystem) {
        unitSystemProvider.updateUnitSystem(unitSystem)
    }
}