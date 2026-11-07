package com.pes.weatherandcitytracker

import com.pes.weatherandcitytracker.data.model.City
import com.pes.weatherandcitytracker.util.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ResourceAndCityTest {

    @Test
    fun testResourceStates() {
        val success = Resource.Success("TestData")
        assertEquals("TestData", success.data)
        assertNull(success.message)

        val error = Resource.Error<String>("Something went wrong")
        assertEquals("Something went wrong", error.message)
        assertNull(error.data)

        val loading = Resource.Loading<String>()
        assertNull(loading.data)
        assertNull(loading.message)
    }

    @Test
    fun testCityDisplayNameFormatting() {
        val cityWithState = City(
            id = 1,
            name = "Bengaluru",
            country = "India",
            state = "Karnataka",
            latitude = 12.97,
            longitude = 77.59
        )
        assertEquals("Bengaluru, Karnataka, India", cityWithState.displayName)

        val cityWithoutState = City(
            id = 2,
            name = "Singapore",
            country = "Singapore",
            state = null,
            latitude = 1.35,
            longitude = 103.81
        )
        assertEquals("Singapore, Singapore", cityWithoutState.displayName)
    }
}

