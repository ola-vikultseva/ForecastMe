package com.example.forecastme.data.providers

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.forecastme.domain.model.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LocationProviderImpl(
    context: Context
) : PreferenceProvider(context), LocationProvider {

    private val _locationFlow = MutableStateFlow(getPreferredLocation())
    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        when (key) {
            LOCATION_KEY -> {
                _locationFlow.value = getPreferredLocation()
            }
        }
    }

    init {
        preferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun observeLocation(): Flow<Location> = _locationFlow.asStateFlow()

    override suspend fun updateLocation(location: Location) {
        val current = getPreferredLocation()
        if (location == current) return
        preferences.edit {
            putString(LOCATION_KEY, Json.encodeToString(location))
        }
    }

    private fun getPreferredLocation(): Location =
        preferences.getString(LOCATION_KEY, null)?.let { Json.decodeFromString(it) } ?: defaultLocation


    private companion object {
        const val LOCATION_KEY = "location"
        val defaultLocation = Location(55.751244, 37.618423, "Moscow")
    }
}