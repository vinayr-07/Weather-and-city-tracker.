package com.pes.weatherandcitytracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pes.weatherandcitytracker.data.local.entity.WeatherHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<WeatherHistoryEntity>>

    @Query("SELECT * FROM weather_history WHERE cityId = :cityId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestByCityId(cityId: Int): WeatherHistoryEntity?

    @Query("SELECT * FROM weather_history WHERE LOWER(cityName) = LOWER(:cityName) ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestByCityName(cityName: String): WeatherHistoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: WeatherHistoryEntity)

    @Query("DELETE FROM weather_history WHERE id = :id")
    suspend fun deleteHistoryById(id: Long)

    @Query("DELETE FROM weather_history")
    suspend fun clearAllHistory()
}

