package com.pes.weatherandcitytracker.data.model

data class WeatherData(
    val city: City,
    val current: CurrentWeather,
    val daily: List<DailyForecast>,
    val isFromCache: Boolean = false,
    val lastUpdatedTimestamp: Long = System.currentTimeMillis()
)

