package com.pes.weatherandcitytracker

import com.pes.weatherandcitytracker.util.WeatherCodeMapper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherCodeMapperTest {

    @Test
    fun testClearSkyDayAndNight() {
        val day = WeatherCodeMapper.map(0, isDay = 1)
        assertEquals("Clear sky", day.description)
        assertTrue(day.iconUrl.contains("01d"))

        val night = WeatherCodeMapper.map(0, isDay = 0)
        assertEquals("Clear sky", night.description)
        assertTrue(night.iconUrl.contains("01n"))
    }

    @Test
    fun testRainCodes() {
        val rain = WeatherCodeMapper.map(63, isDay = 1)
        assertEquals("Moderate rain", rain.description)
        assertTrue(rain.iconUrl.contains("10d"))

        val heavyRain = WeatherCodeMapper.map(65, isDay = 1)
        assertEquals("Heavy rain", heavyRain.description)
    }

    @Test
    fun testThunderstormCode() {
        val storm = WeatherCodeMapper.map(95, isDay = 1)
        assertEquals("Thunderstorm", storm.description)
        assertTrue(storm.iconUrl.contains("11d"))
    }

    @Test
    fun testUnknownCodeFallback() {
        val unknown = WeatherCodeMapper.map(999, isDay = 1)
        assertTrue(unknown.description.startsWith("Unknown"))
    }
}

