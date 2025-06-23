package com.example.forecastme.data.providers

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.forecastme.domain.model.UnitSystem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UnitSystemProviderImpl(
    context: Context
) : PreferenceProvider(context), UnitSystemProvider {

    override val isMetric: Boolean
        get() = getPreferredUnitSystem() == UnitSystem.METRIC

    private var _unitSystem = MutableStateFlow(getPreferredUnitSystem())
    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        when (key) {
            UNIT_SYSTEM_KEY -> {
                _unitSystem.value = getPreferredUnitSystem()
            }
        }
    }

    init {
        preferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun observeUnitSystem(): Flow<UnitSystem> = _unitSystem.asStateFlow()

    override suspend fun updateUnitSystem(unitSystem: UnitSystem) {
        val current = getPreferredUnitSystem()
        if (unitSystem == current) return
        preferences.edit {
            putString(UNIT_SYSTEM_KEY, unitSystem.name)
        }
    }

    private fun getPreferredUnitSystem(): UnitSystem =
        preferences.getString(UNIT_SYSTEM_KEY, null)?.let { UnitSystem.valueOf(it) } ?: defaultUnitSystem

    private companion object {
        const val UNIT_SYSTEM_KEY = "unit_system"
        val defaultUnitSystem = UnitSystem.METRIC
    }
}