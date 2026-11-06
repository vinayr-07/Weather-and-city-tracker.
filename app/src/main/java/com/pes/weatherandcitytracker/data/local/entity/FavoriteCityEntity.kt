package com.pes.weatherandcitytracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_cities")
data class FavoriteCityEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val country: String,
    val state: String?,
    val latitude: Double,
    val longitude: Double,
    val savedAt: Long = System.currentTimeMillis()
)

