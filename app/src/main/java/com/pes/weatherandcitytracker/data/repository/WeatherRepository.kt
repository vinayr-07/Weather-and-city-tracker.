package com.pes.weatherandcitytracker.data.repository

import com.pes.weatherandcitytracker.data.local.PreferencesManager
import com.pes.weatherandcitytracker.data.local.entity.WeatherHistoryEntity
import com.pes.weatherandcitytracker.data.model.City
import com.pes.weatherandcitytracker.data.model.WeatherData
import com.pes.weatherandcitytracker.util.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    /**
     * Search cities by keyword using Open-Meteo Geocoding.
     */
    fun searchCities(query: String): Flow<Resource<List<City>>>

    /**
     * Fetch weather for a given city:
     * Attempts network fetch -> saves snapshot in Room cache -> returns WeatherData.
     * If network fails (offline), falls back to Room cached weather.
     */
    fun getWeather(city: City): Flow<Resource<WeatherData>>

    /**
     * Observable stream of all favorite cities from Room DB.
     */
    fun getFavoriteCities(): Flow<List<City>>

    /**
     * Check if a city is currently marked as favorite.
     */
    fun isFavorite(cityId: Int): Flow<Boolean>

    /**
     * Add or remove a city from Room favorites.
     */
    suspend fun toggleFavorite(city: City)

    /**
     * Observable stream of offline search history from Room DB.
     */
    fun getSearchHistory(): Flow<List<WeatherHistoryEntity>>

    /**
     * Clear all offline search history.
     */
    suspend fun clearSearchHistory()

    /**
     * Access user preferences (unit conversions, last searched city).
     */
    fun getPreferencesManager(): PreferencesManager
}

