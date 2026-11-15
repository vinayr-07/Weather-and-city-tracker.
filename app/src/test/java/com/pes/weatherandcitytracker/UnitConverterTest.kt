package com.pes.weatherandcitytracker

import com.pes.weatherandcitytracker.data.local.PreferencesManager
import com.pes.weatherandcitytracker.ui.util.UnitConverter
import org.junit.Assert.assertEquals
import org.junit.Test

class UnitConverterTest {

    @Test
    fun testCelsiusFormatting() {
        val result = UnitConverter.formatTemperature(25.0, PreferencesManager.UNIT_CELSIUS)
        assertEquals("25.0°C", result)
    }

    @Test
    fun testFahrenheitFormatting() {
        // 0°C = 32°F
        val resultZero = UnitConverter.formatTemperature(0.0, PreferencesManager.UNIT_FAHRENHEIT)
        assertEquals("32.0°F", resultZero)

        // 100°C = 212°F
        val resultBoiling = UnitConverter.formatTemperature(100.0, PreferencesManager.UNIT_FAHRENHEIT)
        assertEquals("212.0°F", resultBoiling)

        // 25°C = 77°F
        val resultRoom = UnitConverter.formatTemperature(25.0, PreferencesManager.UNIT_FAHRENHEIT)
        assertEquals("77.0°F", resultRoom)
    }

    @Test
    fun testKmhFormatting() {
        val result = UnitConverter.formatWindSpeed(15.5, PreferencesManager.UNIT_KMH)
        assertEquals("15.5 km/h", result)
    }

    @Test
    fun testMphFormatting() {
        // 10 km/h approx 6.2 mph
        val result = UnitConverter.formatWindSpeed(10.0, PreferencesManager.UNIT_MPH)
        assertEquals("6.2 mph", result)
    }
}

