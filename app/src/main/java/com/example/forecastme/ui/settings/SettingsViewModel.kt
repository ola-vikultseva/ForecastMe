package com.example.forecastme.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.forecastme.domain.model.Location
import com.example.forecastme.domain.model.UnitSystem
import com.example.forecastme.domain.settings.SettingsInteractor
import com.example.forecastme.ui.settings.model.UnitSystemUiOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    val location: LiveData<Location> = settingsInteractor.observeLocation().asLiveData(viewModelScope.coroutineContext)

    val unitSystem: LiveData<UnitSystemUiOption> = settingsInteractor.observeUnitSystem().map { unitSystem ->
        when (unitSystem) {
            UnitSystem.METRIC -> UnitSystemUiOption.Metric
            UnitSystem.IMPERIAL -> UnitSystemUiOption.Imperial
        }
    }.asLiveData(viewModelScope.coroutineContext)

    fun onUnitSystemSelected(unitSystem: UnitSystem) {
        viewModelScope.launch {
            settingsInteractor.setSelectedUnitSystem(unitSystem)
        }
    }
}
