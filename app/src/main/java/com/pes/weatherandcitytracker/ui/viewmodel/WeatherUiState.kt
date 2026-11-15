package com.pes.weatherandcitytracker.ui.viewmodel

import com.pes.weatherandcitytracker.data.local.PreferencesManager
import com.pes.weatherandcitytracker.data.model.City
import com.pes.weatherandcitytracker.data.model.WeatherData

data class WeatherUiState(
    val searchQuery: String = "",
    val searchResults: List<City> = emptyList(),
    val isSearching: Boolean = false,
    val selectedCity: City? = null,
    val weatherData: WeatherData? = null,
    val isLoading: Boolean = false,
    val isFavorite: Boolean = false,
    val errorMessage: String? = null,
    val tempUnit: String = PreferencesManager.UNIT_CELSIUS,
    val windUnit: String = PreferencesManager.UNIT_KMH
)

