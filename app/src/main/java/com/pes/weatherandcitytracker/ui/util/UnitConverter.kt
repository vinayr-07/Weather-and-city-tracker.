package com.pes.weatherandcitytracker.ui.util

import com.pes.weatherandcitytracker.data.local.PreferencesManager
import java.util.Locale

object UnitConverter {

    /**
     * Converts a Celsius value to the user's preferred unit (°C or °F)
     * and returns a formatted string.
     */
    fun formatTemperature(celsius: Double, unit: String): String {
        return if (unit == PreferencesManager.UNIT_FAHRENHEIT) {
            val fahrenheit = (celsius * 9.0 / 5.0) + 32.0
            String.format(Locale.US, "%.1f°F", fahrenheit)
        } else {
            String.format(Locale.US, "%.1f°C", celsius)
        }
    }

    /**
     * Converts a km/h wind speed to the user's preferred unit (km/h or mph)
     * and returns a formatted string.
     */
    fun formatWindSpeed(speedKmh: Double, unit: String): String {
        return if (unit == PreferencesManager.UNIT_MPH) {
            val mph = speedKmh * 0.621371
            String.format(Locale.US, "%.1f mph", mph)
        } else {
            String.format(Locale.US, "%.1f km/h", speedKmh)
        }
    }
}

