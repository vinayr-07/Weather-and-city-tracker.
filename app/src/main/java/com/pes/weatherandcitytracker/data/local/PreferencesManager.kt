package com.pes.weatherandcitytracker.data.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    private val _tempUnitFlow = MutableStateFlow(getTemperatureUnit())
    val tempUnitFlow: StateFlow<String> = _tempUnitFlow.asStateFlow()

    private val _windUnitFlow = MutableStateFlow(getWindSpeedUnit())
    val windUnitFlow: StateFlow<String> = _windUnitFlow.asStateFlow()

    fun getTemperatureUnit(): String {
        return prefs.getString(KEY_TEMP_UNIT, UNIT_CELSIUS) ?: UNIT_CELSIUS
    }

    fun setTemperatureUnit(unit: String) {
        prefs.edit().putString(KEY_TEMP_UNIT, unit).apply()
        _tempUnitFlow.value = unit
    }

    fun getWindSpeedUnit(): String {
        return prefs.getString(KEY_WIND_UNIT, UNIT_KMH) ?: UNIT_KMH
    }

    fun setWindSpeedUnit(unit: String) {
        prefs.edit().putString(KEY_WIND_UNIT, unit).apply()
        _windUnitFlow.value = unit
    }

    fun getLastCity(): String {
        return prefs.getString(KEY_LAST_CITY, "Bengaluru") ?: "Bengaluru"
    }

    fun setLastCity(cityName: String) {
        prefs.edit().putString(KEY_LAST_CITY, cityName).apply()
    }

    companion object {
        private const val PREFS_NAME = "weather_tracker_prefs"
        const val KEY_TEMP_UNIT = "key_temp_unit"
        const val KEY_WIND_UNIT = "key_wind_unit"
        const val KEY_LAST_CITY = "key_last_city"

        const val UNIT_CELSIUS = "C"
        const val UNIT_FAHRENHEIT = "F"
        const val UNIT_KMH = "KMH"
        const val UNIT_MPH = "MPH"
    }
}

