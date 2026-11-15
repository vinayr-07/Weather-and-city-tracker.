package com.pes.weatherandcitytracker.data.model

data class CurrentWeather(
    val temperatureCelsius: Double,
    val apparentTemperatureCelsius: Double,
    val humidityPercentage: Int,
    val windSpeedKmh: Double,
    val precipitationMm: Double,
    val weatherCode: Int,
    val conditionDescription: String,
    val iconUrl: String,
    val isDay: Boolean
)

