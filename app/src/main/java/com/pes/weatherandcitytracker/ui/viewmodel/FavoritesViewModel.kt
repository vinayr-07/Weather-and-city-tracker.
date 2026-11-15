package com.pes.weatherandcitytracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pes.weatherandcitytracker.data.model.City
import com.pes.weatherandcitytracker.data.repository.WeatherRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    val favorites: StateFlow<List<City>> = repository.getFavoriteCities()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun removeFavorite(city: City) {
        viewModelScope.launch {
            repository.toggleFavorite(city)
        }
    }
}

