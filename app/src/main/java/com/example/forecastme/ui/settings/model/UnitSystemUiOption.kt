package com.example.forecastme.ui.settings.model

import com.example.forecastme.R
import com.example.forecastme.domain.model.UnitSystem

sealed class UnitSystemUiOption(val unitSystem: UnitSystem, val labelResId: Int) {
    data object Metric : UnitSystemUiOption(UnitSystem.METRIC, R.string.unit_system_metric)
    data object Imperial : UnitSystemUiOption(UnitSystem.IMPERIAL, R.string.unit_system_imperial)
    companion object {
        fun all() = listOf(Metric, Imperial)
    }
}
