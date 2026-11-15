package com.pes.weatherandcitytracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pes.weatherandcitytracker.data.repository.WeatherRepository

class ViewModelFactory(
    private val repository: WeatherRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(WeatherViewModel::class.java) -> {
                WeatherViewModel(repository) as T
            }
            modelClass.isAssignableFrom(FavoritesViewModel::class.java) -> {
                FavoritesViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

