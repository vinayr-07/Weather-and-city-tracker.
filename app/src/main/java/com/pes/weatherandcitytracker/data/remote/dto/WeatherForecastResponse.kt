package com.pes.weatherandcitytracker.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherForecastResponse(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("current")
    val current: CurrentWeatherDto? = null,
    @SerializedName("daily")
    val daily: DailyWeatherDto? = null
)

data class CurrentWeatherDto(
    @SerializedName("time")
    val time: String? = null,
    @SerializedName("temperature_2m")
    val temperature2m: Double = 0.0,
    @SerializedName("relative_humidity_2m")
    val relativeHumidity2m: Int = 0,
    @SerializedName("apparent_temperature")
    val apparentTemperature: Double = 0.0,
    @SerializedName("is_day")
    val isDay: Int = 1,
    @SerializedName("precipitation")
    val precipitation: Double = 0.0,
    @SerializedName("weather_code")
    val weatherCode: Int = 0,
    @SerializedName("wind_speed_10m")
    val windSpeed10m: Double = 0.0
)

data class DailyWeatherDto(
    @SerializedName("time")
    val time: List<String>? = null,
    @SerializedName("weather_code")
    val weatherCode: List<Int>? = null,
    @SerializedName("temperature_2m_max")
    val temperature2mMax: List<Double>? = null,
    @SerializedName("temperature_2m_min")
    val temperature2mMin: List<Double>? = null
)

