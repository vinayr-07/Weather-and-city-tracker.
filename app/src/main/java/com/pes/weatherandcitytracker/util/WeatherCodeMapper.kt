package com.pes.weatherandcitytracker.util

/**
 * Maps WMO (World Meteorological Organization) weather interpretation codes
 * returned by the Open-Meteo API into human-friendly descriptions and icon URLs.
 */
object WeatherCodeMapper {

    data class WeatherCondition(
        val description: String,
        val iconUrl: String
    )

    fun map(wmoCode: Int, isDay: Int = 1): WeatherCondition {
        val dayNightSuffix = if (isDay == 1) "d" else "n"

        return when (wmoCode) {
            0 -> WeatherCondition(
                description = "Clear sky",
                iconUrl = "https://openweathermap.org/img/wn/01$dayNightSuffix@2x.png"
            )
            1 -> WeatherCondition(
                description = "Mainly clear",
                iconUrl = "https://openweathermap.org/img/wn/02$dayNightSuffix@2x.png"
            )
            2 -> WeatherCondition(
                description = "Partly cloudy",
                iconUrl = "https://openweathermap.org/img/wn/03$dayNightSuffix@2x.png"
            )
            3 -> WeatherCondition(
                description = "Overcast",
                iconUrl = "https://openweathermap.org/img/wn/04$dayNightSuffix@2x.png"
            )
            45, 48 -> WeatherCondition(
                description = "Fog / Depositing rime fog",
                iconUrl = "https://openweathermap.org/img/wn/50$dayNightSuffix@2x.png"
            )
            51, 53, 55 -> WeatherCondition(
                description = "Drizzle",
                iconUrl = "https://openweathermap.org/img/wn/09$dayNightSuffix@2x.png"
            )
            56, 57 -> WeatherCondition(
                description = "Freezing Drizzle",
                iconUrl = "https://openweathermap.org/img/wn/13$dayNightSuffix@2x.png"
            )
            61, 63 -> WeatherCondition(
                description = "Moderate rain",
                iconUrl = "https://openweathermap.org/img/wn/10$dayNightSuffix@2x.png"
            )
            65 -> WeatherCondition(
                description = "Heavy rain",
                iconUrl = "https://openweathermap.org/img/wn/10$dayNightSuffix@2x.png"
            )
            66, 67 -> WeatherCondition(
                description = "Freezing Rain",
                iconUrl = "https://openweathermap.org/img/wn/13$dayNightSuffix@2x.png"
            )
            71, 73, 75, 77 -> WeatherCondition(
                description = "Snow fall",
                iconUrl = "https://openweathermap.org/img/wn/13$dayNightSuffix@2x.png"
            )
            80, 81, 82 -> WeatherCondition(
                description = "Rain showers",
                iconUrl = "https://openweathermap.org/img/wn/09$dayNightSuffix@2x.png"
            )
            85, 86 -> WeatherCondition(
                description = "Snow showers",
                iconUrl = "https://openweathermap.org/img/wn/13$dayNightSuffix@2x.png"
            )
            95 -> WeatherCondition(
                description = "Thunderstorm",
                iconUrl = "https://openweathermap.org/img/wn/11$dayNightSuffix@2x.png"
            )
            96, 99 -> WeatherCondition(
                description = "Thunderstorm with hail",
                iconUrl = "https://openweathermap.org/img/wn/11$dayNightSuffix@2x.png"
            )
            else -> WeatherCondition(
                description = "Unknown ($wmoCode)",
                iconUrl = "https://openweathermap.org/img/wn/03$dayNightSuffix@2x.png"
            )
        }
    }
}

