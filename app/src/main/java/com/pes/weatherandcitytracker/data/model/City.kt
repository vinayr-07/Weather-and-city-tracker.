package com.pes.weatherandcitytracker.data.model

data class City(
    val id: Int,
    val name: String,
    val country: String,
    val state: String?,
    val latitude: Double,
    val longitude: Double,
    val isFavorite: Boolean = false
) {
    val displayName: String
        get() = if (!state.isNullOrBlank()) "$name, $state, $country" else "$name, $country"
}

