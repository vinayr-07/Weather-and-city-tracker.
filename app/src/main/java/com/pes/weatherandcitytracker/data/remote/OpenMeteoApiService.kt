package com.pes.weatherandcitytracker.data.remote

import com.pes.weatherandcitytracker.data.remote.dto.GeocodingResponse
import com.pes.weatherandcitytracker.data.remote.dto.WeatherForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface OpenMeteoApiService {

    /**
     * Search cities by name using Open-Meteo Geocoding API.
     * Full URL override used because geocoding is hosted on a separate subdomain.
     */
    @GET("https://geocoding-api.open-meteo.com/v1/search")
    suspend fun searchCities(
        @Query("name") name: String,
        @Query("count") count: Int = 10,
        @Query("language") language: String = "en",
        @Query("format") format: String = "json"
    ): Response<GeocodingResponse>

    /**
     * Fetch current weather & 7-day forecast for given latitude and longitude coordinates.
     */
    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,weather_code,wind_speed_10m",
        @Query("daily") daily: String = "weather_code,temperature_2m_max,temperature_2m_min",
        @Query("timezone") timezone: String = "auto"
    ): Response<WeatherForecastResponse>
}

