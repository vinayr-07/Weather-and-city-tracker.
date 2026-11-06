package com.pes.weatherandcitytracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_history")
data class WeatherHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cityId: Int,
    val cityName: String,
    val country: String,
    val state: String?,
    val latitude: Double,
    val longitude: Double,
    val temperatureCelsius: Double,
    val apparentTemperatureCelsius: Double,
    val weatherCode: Int,
    val conditionDescription: String,
    val humidityPercentage: Int,
    val windSpeedKmh: Double,
    val iconUrl: String,
    val isDay: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

