package com.pes.weatherandcitytracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pes.weatherandcitytracker.data.local.entity.WeatherHistoryEntity
import com.pes.weatherandcitytracker.data.repository.WeatherRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val prefs = repository.getPreferencesManager()

    val tempUnit: StateFlow<String> = prefs.tempUnitFlow
    val windUnit: StateFlow<String> = prefs.windUnitFlow

    val searchHistory: StateFlow<List<WeatherHistoryEntity>> = repository.getSearchHistory()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setTemperatureUnit(unit: String) {
        prefs.setTemperatureUnit(unit)
    }

    fun setWindSpeedUnit(unit: String) {
        prefs.setWindSpeedUnit(unit)
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            repository.clearSearchHistory()
        }
    }
}

