package com.pes.weatherandcitytracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pes.weatherandcitytracker.data.model.City
import com.pes.weatherandcitytracker.data.repository.WeatherRepository
import com.pes.weatherandcitytracker.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private var favoriteObservationJob: Job? = null

    init {
        // Observe preferences for unit changes
        val prefs = repository.getPreferencesManager()
        viewModelScope.launch {
            prefs.tempUnitFlow.collectLatest { unit ->
                _uiState.update { it.copy(tempUnit = unit) }
            }
        }
        viewModelScope.launch {
            prefs.windUnitFlow.collectLatest { unit ->
                _uiState.update { it.copy(windUnit = unit) }
            }
        }

        // Load default city (Bengaluru or last searched)
        val defaultCityName = prefs.getLastCity()
        loadInitialCity(defaultCityName)
    }

    private fun loadInitialCity(cityName: String) {
        viewModelScope.launch {
            repository.searchCities(cityName).collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val firstCity = resource.data?.firstOrNull() ?: City(
                            id = 1277333,
                            name = "Bengaluru",
                            country = "India",
                            state = "Karnataka",
                            latitude = 12.97194,
                            longitude = 77.59369
                        )
                        selectCity(firstCity)
                    }
                    is Resource.Error -> {
                        val fallbackCity = City(
                            id = 1277333,
                            name = "Bengaluru",
                            country = "India",
                            state = "Karnataka",
                            latitude = 12.97194,
                            longitude = 77.59369
                        )
                        selectCity(fallbackCity)
                    }
                    is Resource.Loading -> Unit
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()

        if (query.trim().length < 2) {
            _uiState.update { it.copy(searchResults = emptyList(), isSearching = false) }
            return
        }

        searchJob = viewModelScope.launch {
            delay(400) // Debounce network queries
            repository.searchCities(query).collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isSearching = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                searchResults = resource.data.orEmpty(),
                                isSearching = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                searchResults = emptyList(),
                                isSearching = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun selectCity(city: City) {
        _uiState.update {
            it.copy(
                selectedCity = city,
                searchQuery = "",
                searchResults = emptyList(),
                isSearching = false
            )
        }
        observeFavoriteStatus(city.id)
        loadWeatherForCity(city)
    }

    private fun observeFavoriteStatus(cityId: Int) {
        favoriteObservationJob?.cancel()
        favoriteObservationJob = viewModelScope.launch {
            repository.isFavorite(cityId).collectLatest { isFav ->
                _uiState.update { it.copy(isFavorite = isFav) }
            }
        }
    }

    fun refreshWeather() {
        _uiState.value.selectedCity?.let { loadWeatherForCity(it) }
    }

    private fun loadWeatherForCity(city: City) {
        viewModelScope.launch {
            repository.getWeather(city).collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                weatherData = resource.data,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun toggleFavorite() {
        val city = _uiState.value.selectedCity ?: return
        viewModelScope.launch {
            repository.toggleFavorite(city)
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

