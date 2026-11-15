package com.pes.weatherandcitytracker.data.model

data class DailyForecast(
    val date: String,
    val maxTemperatureCelsius: Double,
    val minTemperatureCelsius: Double,
    val weatherCode: Int,
    val conditionDescription: String,
    val iconUrl: String
)

